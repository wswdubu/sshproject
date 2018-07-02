package pfc.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pfc.bean.Privilege;
import pfc.bean.Role;
import pfc.bean.Variable;
import pfc.service.privilege.PrivilegeService;
import pfc.service.role.RoleService;
import pfc.service.variable.VariableService;

public class GenerateTool {
	// 获得缺省的系统区域
	static Locale locale = Locale.getDefault();
	// 获得资源文件
	static ResourceBundle rb = ResourceBundle.getBundle("message", locale);

	public static void generateservice(String pkgname, String clazzname,
			String ifename) {
		StringBuffer content = new StringBuffer();
		content.append("package pfc.service." + pkgname + ";");
		content.append("\r\n");
		content.append("\r\n");
		content.append("\r\n");
		content.append("import pfc.bean." + clazzname + ";");
		content.append("\r\n");
		content.append("import pfc.service.base.DAO;");
		content.append("\r\n");
		content.append("\r\n");
		content.append("\r\n");
		content.append("public interface " + clazzname + "Service extends DAO<"
				+ clazzname + "> {");
		content.append("\r\n");
		content.append("\r\n");
		content.append("}");
		bufferwriterToFile(content.toString(), ifename);
	}

	public static void append(StringBuffer sb, String str, int num) {
		sb.append(str);
		for (int i = 0; i < num; i++) {
			sb.append("\r\n");
		}
	}

	public static String generateSetGet(String clazzname, String lname) {
		StringBuffer se = new StringBuffer();
		append(se,
				"	public " + clazzname + " get" + returnUstr(lname) + "() {", 1);
		append(se, "		return " + lname + ";", 1);
		append(se, "	}", 2);
		append(se, "	public void set" + returnUstr(lname) + "(" + clazzname
				+ " " + lname + ") {", 1);
		append(se, "		this." + lname + " = " + lname + ";", 1);
		append(se, "	}", 1);
		return se.toString();
	}

	public static String returnUstr(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	public static String returnLstr(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String returnI18n(String str) {
		String ss;
		try {
			ss = rb.getString(str);
		} catch (Exception e) {
			ss = str;
		}

		return ss;
	}

	public static void addElement(String springxml, String lc, String uc,
			HashSet<String> serviceset) throws Exception {
		SAXBuilder builder = new SAXBuilder();
		InputStream file = new FileInputStream(springxml);
		Document doc = builder.build(file);
		Element root = doc.getRootElement();
		Namespace ns1 = Namespace
				.getNamespace("http://www.springframework.org/schema/beans");
		List<Element> list = root.getChildren();
		for (Element e : list) {
			if (e.getAttributeValue("id").equals(lc + "Action")
					|| e.getAttributeValue("id").equals(uc + "Service")) {
				root.removeContent(e);
				break;
			}
		}
		for (Element e : list) {
			if (e.getAttributeValue("id").equals(lc + "Action")
					|| e.getAttributeValue("id").equals(uc + "Service")) {
				root.removeContent(e);
				break;
			}
		}
		// 添加新元素
		Element elementservice = new Element("bean");
		elementservice.setAttribute("id", uc + "Service");
		elementservice.setAttribute("class", "pfc.service." + lc + ".impl."
				+ uc + "ServiceImpl");
		Element element1 = new Element("property");
		element1.setAttribute("name", "sessionFactory");
		element1.setAttribute("ref", "sessionFactory");
		elementservice.addContent(element1);

		Element elementaction = new Element("bean");
		elementaction.setAttribute("id", lc + "Action");
		elementaction.setAttribute("class", "pfc.web." + uc + "Action");
		Element element2 = new Element("property");
		element2.setAttribute("name", lc + "Service");
		element2.setAttribute("ref", uc + "Service");
		for (String sic : serviceset) {
			Element element3 = new Element("property");
			element3.setAttribute("name", sic.toLowerCase() + "Service");
			element3.setAttribute("ref", sic + "Service");
			elementaction.addContent(element3);
			element3.setNamespace(ns1);
		}
		elementaction.addContent(element2);
		root.addContent(elementaction);
		root.addContent(elementservice);
		elementaction.setNamespace(ns1);

		element2.setNamespace(ns1);
		element1.setNamespace(ns1);
		elementservice.setNamespace(ns1);
		doc.setRootElement(root);

		// 文件处理
		XMLOutputter out = new XMLOutputter(FormatXML());
		out.output(doc, new FileOutputStream(springxml));
	}

	public static void bufferwriterToFile(String str, String filename) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename));
			bw.write(str);
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<String> getClazzPros(String clazzname) {
		List<String> stlist = new ArrayList<String>();
		String classPath = "pfc.bean." + clazzname;
		Class userCla = null;
		try {
			userCla = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (!f.getName().equals("id")) {
				String type = f.getType().toString();// 得到此属性的类型
				if (type.endsWith("String")) {
					stlist.add(f.getName() + "@String");
				} else if (type.endsWith("Integer") || type.endsWith("int")) {
					stlist.add(f.getName() + "@Integer");
				} else if (type.endsWith("Date")
						&& !(f.getName().contains("Start") || f.getName()
								.contains("End"))) {
					stlist.add(f.getName() + "@Date");
				} else if (type.endsWith("Timestamp")
						&& !(f.getName().contains("Start") || f.getName()
								.contains("End"))) {
					stlist.add(f.getName() + "@Timestamp");
				}
			}

		}
		return stlist;
	}

	public static List<String> getClazzProsO(String clazzname) {
		File directory = new File("");
		String hbmpath = directory.getAbsolutePath() + "//src//main//java//pfc//bean//"
				+ clazzname + ".hbm.xml";
		List<String> stlist = new ArrayList<String>();
		String classPath = "pfc.bean." + clazzname;
		Class userCla = null;
		try {
			userCla = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (!f.getName().equals("id")) {
				String type = f.getType().toString();// 得到此属性的类型
				if (type.contains("pfc.bean")) {
					stlist.add(f.getName() + "@one" + "@"
							+ type.substring(type.lastIndexOf(".") + 1));
				} else if (type.endsWith("Set")) {
					String clz = ParseHiberXml
							.getSetClazz(hbmpath, f.getName());
					if (clz != null)
						stlist.add(f.getName()
								+ "@set@"
								+ ParseHiberXml.getSetClazz(hbmpath, f
										.getName()));
				}
			}

		}
		return stlist;
	}

	public static void generatetime(String clazzname, String path) {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		boolean hasstart = false;
		try {
			br = new BufferedReader(new FileReader(path + clazzname + ".java"));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.contains("Start")) {
					hasstart = true;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (!hasstart) {
			String classPath = "pfc.bean." + clazzname;
			Class userCla = null;
			try {
				userCla = Class.forName(classPath);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			StringBuffer st = new StringBuffer();
			Field[] fs = userCla.getDeclaredFields();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); // 设置些属性是可以访问的
				if (!f.getName().equals("id")) {
					String type = f.getType().toString();// 得到此属性的类型
					if (type.endsWith("Date")) {
						st.append("	private Date " + f.getName() + "Start;")
								.append("\r\n");
						st
								.append(
										generateSetGet("Date", f.getName()
												+ "Start")).append("\r\n");
						st.append("	private Date " + f.getName() + "End;")
								.append("\r\n");
						st.append(generateSetGet("Date", f.getName() + "End"))
								.append("\r\n");
					} else if (type.endsWith("Timestamp")) {
						st.append(
								"	private Timestamp " + f.getName() + "Start;")
								.append("\r\n");
						st.append(
								generateSetGet("Timestamp", f.getName()
										+ "Start")).append("\r\n");
						st.append("	private Timestamp " + f.getName() + "End;")
								.append("\r\n");
						st
								.append(
										generateSetGet("Timestamp", f.getName()
												+ "End")).append("\r\n");
					}
				}

			}
			br = null;
			try {
				br = new BufferedReader(new FileReader(path + clazzname
						+ ".java"));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (!line.equals("}")) {
						sb.append(line).append("\r\n");
					} else {
						sb.append(st.toString()).append("\r\n");
						sb.append("}");
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			bufferwriterToFile(sb.toString(), path + clazzname + ".java");
		}
	}

	public static HashSet<String> getClazzService(String clazzname) {
		File directory = new File("");
		String hbmpath = directory.getAbsolutePath() + "//src/main//java//pfc//bean//"
				+ clazzname + ".hbm.xml";
		HashSet<String> serviceset = new HashSet<String>();
		String classPath = "pfc.bean." + clazzname;
		Class userCla = null;
		try {
			userCla = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (!f.getName().equals("id")) {
				String type = f.getType().toString();// 得到此属性的类型
				if (type.contains("pfc.bean")) {
					serviceset.add(type.substring(type.lastIndexOf(".") + 1));
				} else if (type.endsWith("Set")) {
					String clz = ParseHiberXml
							.getSetClazz(hbmpath, f.getName());
					if (clz != null)
						serviceset.add(ParseHiberXml.getSetClazz(hbmpath, f
								.getName()));
				}
			}

		}
		return serviceset;
	}

	public static String getDisplayname(String clazzname) {
		String classPath = "pfc.bean." + clazzname;
		Class userCla = null;
		try {
			userCla = Class.forName(classPath);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (!f.getName().equals("id")) {
				String type = f.getType().toString();// 得到此属性的类型
				if (type.endsWith("String")) {
					if (f.getName().contains("name")
							|| f.getName().contains("title")
							|| f.getName().contains("Name")
							|| f.getName().contains("Title")
							|| f.getName().contains("content")
							|| f.getName().contains("Content")) {
						return f.getName();
					}
				}
			}

		}
		return null;
	}

	public static void generateprivilege(List<String> plist) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		PrivilegeService ps = (PrivilegeService) ctx
				.getBean("PrivilegeService");
		VariableService vs = (VariableService) ctx.getBean("VariableService");
		RoleService rs = (RoleService) ctx.getBean("RoleService");
		Role role = rs.queryByPropertye("rolename", "系统管理员").get(0);
		List<Privilege> pslist = new ArrayList<Privilege>();
		List<Privilege> pl = ps.getListByCriteria(null, null, "type", "desc");
		int starttype = 0;
		if (pl.size() > 0) {
			starttype = pl.get(0).getType();
		}
		for (int i = 0; i < plist.size(); i++) {
			boolean haspri = false;
			List<Privilege> lp = ps.queryByPropertye("priname", plist.get(i)
					.toLowerCase()
					+ "list");
			if (lp.size() > 0) {
				haspri = true;
			}
			if (!haspri) {
				int type = starttype + i + 1;
				Variable vb = new Variable();
				vb.setReturnname(plist.get(i));
				vb.setVarname("privilege.type");
				vb.setVarvalue(String.valueOf((type)));
				vs.save(vb);

				String pname = plist.get(i).toLowerCase();
				Privilege pri = new Privilege();
				pri.setPriname(pname + "list");
				pri.setPriurl("/" + pname + "_list.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "listlook");
				pri.setPriurl("/" + pname + "_listlook.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "listlooks");
				pri.setPriurl("/" + pname + "_listlooks.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "insertUI");
				pri.setPriurl("/" + pname + "_insertUI.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "insert");
				pri.setPriurl("/" + pname + "_insert.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "updateUI");
				pri.setPriurl("/" + pname + "_updateUI.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "update");
				pri.setPriurl("/" + pname + "_update.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "view");
				pri.setPriurl("/" + pname + "_view.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);

				pri = new Privilege();
				pri.setPriname(pname + "del");
				pri.setPriurl("/" + pname + "_del.do");
				pri.setType(type);
				ps.save(pri);
				pslist.add(pri);
				System.out.println(pname + "权限生成成功..");
			} else {
				System.out.println("已经有此类型权限:" + plist.get(i).toLowerCase());
			}
		}
		List<Privilege> psn = ps.queryByObjectPropertye("roles.id", role
				.getId(), "roles");
		for (Privilege pp : psn) {
			pslist.add(pp);
		}
		role.setPrivileges(new HashSet<Privilege>(pslist));
		rs.update(role);
	}

	public static void generatelogin(String pagelogin){
		StringBuffer content = new StringBuffer();
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>",1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<!DOCTYPE html>",1);
		append(content, "<html lang=\"en\" class=\"no-js\">",1);
		append(content, "",1);
		append(content, "    <head>",1);
		append(content, "",1);
		append(content, "        <meta charset=\"utf-8\">",1);
		append(content, "        <title>"+"管理系统</title>",1);
		append(content, "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">",1);
		append(content, "        <meta name=\"description\" content=\"\">",1);
		append(content, "        <meta name=\"author\" content=\"\">",1);
		append(content, "",1);
		append(content, "        <!-- CSS -->",1);
		append(content, "        <link rel=\"stylesheet\" href=\"assets/css/reset.css\">",1);
		append(content, "        <link rel=\"stylesheet\" href=\"assets/css/supersized.css\">",1);
		append(content, "        <link rel=\"stylesheet\" href=\"assets/css/style.css\">",1);
		append(content, "",1);
		append(content, "        <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->",1);
		append(content, "        <!--[if lt IE 9]>",1);
		append(content, "            <script src=\"http://html5shim.googlecode.com/svn/trunk/html5.js\"></script>",1);
		append(content, "        <![endif]-->",1);
		append(content, "",1);
		append(content, "    </head>",1);
		append(content, "",1);
		append(content, "    <body>",1);
		append(content, "",1);
		append(content, "        <div class=\"page-container\">",1);
		append(content, "            <h1>"+"管理系统登录</h1>",1);
		append(content, "            <form action=\""+"login_login.do\" method=\"post\">",1);
		append(content, "                <input type=\"text\" name=\"username\" class=\"username\" placeholder=\"Username\">",1);
		append(content, "                <input type=\"password\" name=\"password\" class=\"password\" placeholder=\"Password\">",1);
		append(content, "                <button type=\"submit\">登录</button>",1);
		append(content, "                <div class=\"error\"><span>+</span></div>",1);
		append(content, "            </form>",1);
		append(content, "            <div class=\"connect\">",1);
		append(content, "                <p>联系我</p>",1);
		append(content, "                <p>",1);
		append(content, "                    <a class=\"facebook\" href=\"\"></a>",1);
		append(content, "                    <a class=\"twitter\" href=\"\"></a>",1);
		append(content, "                </p>",1);
		append(content, "            </div>",1);
		append(content, "        </div>",1);
		append(content, "		",1);
		append(content, "        <script src=\"assets/js/jquery-1.8.2.min.js\"></script>",1);
		append(content, "        <script src=\"assets/js/supersized.3.2.7.min.js\"></script>",1);
		append(content, "        <script src=\"assets/js/supersized-init.js\"></script>",1);
		append(content, "        <script src=\"assets/js/scripts.js\"></script>",1);
		append(content, "",1);
		append(content, "    </body>",1);
		append(content, "",1);
		append(content, "</html>",1);
		append(content, "",1);
		bufferwriterToFile(content.toString(), pagelogin);

	}
	public static Format FormatXML() {
		// 格式化生成的xml文件，如果不进行格式化的话，生成的xml文件将会是很长的一行...
		Format format = Format.getCompactFormat();
		format.setEncoding("utf-8");
		format.setIndent(" ");
		return format;
	}

	public static void generateserviceimpl(String pkgname, String clazzname,
			String ifeimpl) {
		StringBuffer content = new StringBuffer();
		content.append("package pfc.service." + pkgname + ".impl;");
		content.append("\r\n");
		content.append("\r\n");
		content.append("\r\n");
		content.append("import pfc.bean." + clazzname + ";");
		content.append("\r\n");
		content.append("import pfc.service.base.DaoSupport;");
		content.append("\r\n");
		content.append("import pfc.service." + pkgname + "." + clazzname
				+ "Service;");
		content.append("\r\n").append("\r\n");
		content.append("public class " + clazzname
				+ "ServiceImpl extends DaoSupport<" + clazzname
				+ "> implements " + clazzname + "Service {");
		content.append("\r\n").append("\r\n");
		content.append("}");
		bufferwriterToFile(content.toString(), ifeimpl);
	}

	public static void generateindex(List<String> clist, boolean ifpri ) {
		// 生成index.jsp
		File directory = new File("");
		String indexpath = directory.getAbsolutePath()
				+ "\\WebRoot\\pages\\index.jsp";
		StringBuffer content = new StringBuffer();
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\"%>", 1);
		append(
				content,
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">",
				1);
		append(content, "<html xmlns=\"http://www.w3.org/1999/xhtml\">", 1);
		append(content, "<head>", 1);
		append(
				content,
				"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />",
				1);
		append(content, "<title>"+ "管理系统</title>", 1);
		append(content, "", 1);
		append(
				content,
				"<link href=\"themes/default/style.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>",
				1);
		append(
				content,
				"<link href=\"themes/css/core.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>",
				1);
		append(
				content,
				"<link href=\"themes/css/print.css\" rel=\"stylesheet\" type=\"text/css\" media=\"print\"/>",
				1);
		append(
				content,
				"<link href=\"uploadify/css/uploadify.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>",
				1);
		append(content, "<!--[if IE]>", 1);
		append(
				content,
				"<link href=\"themes/css/ieHack.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>",
				1);
		append(content, "<![endif]-->", 1);
		append(content, "", 1);
		append(content, "<!--[if lte IE 9]>", 1);
		append(
				content,
				"<script src=\"js/speedup.js\" type=\"text/javascript\"></script>",
				1);
		append(content, "<![endif]-->", 1);
		append(content, "", 1);
		append(
				content,
				"<script src=\"js/jquery-1.7.2.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/jquery.cookie.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/jquery.validate.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/jquery.bgiframe.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"xheditor/xheditor-1.2.1.min.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"xheditor/xheditor_lang/zh-cn.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"uploadify/scripts/jquery.uploadify.js\" type=\"text/javascript\"></script>",
				1);
		append(content, "", 1);
		append(
				content,
				"<!-- svg图表  supports Firefox 3.0+, Safari 3.0+, Chrome 5.0+, Opera 9.5+ and Internet Explorer 6.0+ -->",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/raphael.js\"></script>",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/g.raphael.js\"></script>",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/g.bar.js\"></script>",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/g.line.js\"></script>",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/g.pie.js\"></script>",
				1);
		append(
				content,
				"<script type=\"text/javascript\" src=\"chart/g.dot.js\"></script>",
				1);
		append(content, "", 1);
		append(
				content,
				"<script src=\"js/dwz.core.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.util.date.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.validate.method.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.regional.zh.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.barDrag.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.drag.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.tree.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.accordion.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.ui.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.theme.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.switchEnv.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.alertMsg.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.contextmenu.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.navTab.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.tab.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.tab.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.resize.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.dialog.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.dialogDrag.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.sortDrag.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.cssTable.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.stable.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.taskBar.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.ajax.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.pagination.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.database.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.datepicker.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.effects.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.panel.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.checkbox.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.history.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.combox.js\" type=\"text/javascript\"></script>",
				1);
		append(
				content,
				"<script src=\"js/dwz.print.js\" type=\"text/javascript\"></script>",
				1);
		append(content, "<!--", 1);
		append(
				content,
				"<script src=\"bin/dwz.min.js\" type=\"text/javascript\"></script>",
				1);
		append(content, "-->", 1);
		append(
				content,
				"<script src=\"js/dwz.regional.zh.js\" type=\"text/javascript\"></script>",
				1);
		append(content, "<%@include file=\"/pages/common/base.jsp\"%>", 1);
		append(content, "<script type=\"text/javascript\">", 1);
		append(content, "$(function(){", 1);
		append(content, "	DWZ.init(\"dwz.frag.xml\", {", 1);
		append(
				content,
				"		loginUrl:\"login_dialog.html\", loginTitle:\"登录\",	// 弹出登录对话框",
				1);
		append(content, "//		loginUrl:\"login.html\",	// 跳到登录页面", 1);
		append(content,
				"		statusCode:{ok:200, error:300, timeout:301}, //【可选】", 1);
		append(
				content,
				"		pageInfo:{pageNum:\"pageNum\", numPerPage:\"numPerPage\", orderField:\"orderField\", orderDirection:\"orderDirection\"}, //【可选】",
				1);
		append(content, "		debug:false,	// 调试模式 【true|false】", 1);
		append(content, "		callback:function(){", 1);
		append(content, "			initEnv();", 1);
		append(
				content,
				"			$(\"#themeList\").theme({themeBase:\"themes\"}); // themeBase 相对于index页面的主题base路径",
				1);
		append(content, "		}", 1);
		append(content, "	});", 1);
		append(content, "});", 1);
		append(content, "", 1);
		append(content, "</script>", 1);
		append(content, "</head>", 1);
		append(content, "", 1);
		append(content, "<body scroll=\"no\">", 1);
		append(content, "	<div id=\"layout\">", 1);
		append(content, "		<div id=\"header\">", 1);
		append(content, "			<div class=\"headerNav\">", 1);
		append(content, "				<img src=\"\" alt=\"\" height=\"65\"/>", 1);
		append(content, "				<ul class=\"nav\">", 1);
		append(
				content,
				"					<li><a href=\"changepwd.html\" target=\"dialog\" width=\"600\">修改密码</a></li>",
				1);
		append(content, "					<li><a href=\"<%=basePath%>" + "login_logout.do\">退出</a></li>", 1);
		append(content, "				</ul>", 1);
		append(content, "				<ul class=\"themeList\" id=\"themeList\">", 1);
		append(
				content,
				"					<li theme=\"azure\"><div class=\"selected\">天蓝</div></li>",
				1);
		append(content, "					<li theme=\"default\"><div>蓝色</div></li>", 1);
		append(content, "					<li theme=\"green\"><div>绿色</div></li>", 1);
		append(content, "					<!--<li theme=\"red\"><div>红色</div></li>-->", 1);
		append(content, "					<li theme=\"purple\"><div>紫色</div></li>", 1);
		append(content, "					<li theme=\"silver\"><div>银色</div></li>", 1);
		append(content, "					", 1);
		append(content, "				</ul>", 1);
		append(content, "			</div>", 1);
		append(content, "", 1);
		append(content, "			<!-- navMenu -->", 1);
		append(content, "			", 1);
		append(content, "		</div>", 1);
		append(content, "", 1);
		append(content, "		<div id=\"leftside\">", 1);
		append(content, "			<div id=\"sidebar_s\">", 1);
		append(content, "				<div class=\"collapse\">", 1);
		append(content, "					<div class=\"toggleCollapse\"><div></div></div>",
				1);
		append(content, "				</div>", 1);
		append(content, "			</div>", 1);
		append(content, "			<div id=\"sidebar\">", 1);
		append(
				content,
				"				<div class=\"toggleCollapse\"><h2>主菜单</h2><div>收缩</div></div>",
				1);
		append(content, "", 1);
		append(content, "				<div class=\"accordion\" fillSpace=\"sidebar\">",
				1);
		append(content, "					<div class=\"accordionHeader\">", 1);
		append(content, "						<h2><span>Folder</span>" 
				+ "管理系统</h2>", 1);
		append(content, "					</div> ", 1);
		append(content, "					<div class=\"accordionContent\">", 1);
		append(content, "						<ul class=\"tree treeFolder\">", 1);
		append(content,
				"							<li><a><tsp:transproname proname=\"mymenu\"/></a>", 1);
		append(content, "								<ul>", 1);
		for (int i = 0; i < clist.size(); i++) {
			if (ifpri) {
				append(content, "									<shiro:hasPermission name=\""
						+ clist.get(i).toLowerCase() + "list\">", 1);
			}
			append(content, "									<li><a href=\"<%=basePath%>"
					+ clist.get(i).toLowerCase() + "_list.do?"
					+ clist.get(i).toLowerCase()
					+ ".id=0\" target=\"navTab\" rel=\""
					+ clist.get(i).toLowerCase()
					+ "list\"><tsp:transproname proname=\""
					+ clist.get(i).toLowerCase() + "menu\"/></a></li>", 1);
			if (ifpri) {
				append(content, "									</shiro:hasPermission>", 1);
			}
		}
		clist = new ArrayList<String>();
		clist.add("User");
		clist.add("Role");
		clist.add("Privilege");
		clist.add("Variable");
		for (int i = 0; i < clist.size(); i++) {
			if (!clist.get(i).equals("Variable")) {
				append(content, "									<shiro:hasPermission name=\""
						+ clist.get(i).toLowerCase() + "list\">", 1);
				append(content, "									<li><a href=\"<%=basePath%>"
						+ clist.get(i).toLowerCase() + "_list.do?"
						+ clist.get(i).toLowerCase()
						+ ".id=0\" target=\"navTab\" rel=\""
						+ clist.get(i).toLowerCase()
						+ "list\"><tsp:transproname proname=\""
						+ clist.get(i).toLowerCase() + "menu\"/></a></li>", 1);
				append(content, "									</shiro:hasPermission>", 1);
			} else {
				append(content, "									<li><a href=\"<%=basePath%>"
						+ clist.get(i).toLowerCase() + "_list.do?"
						+ clist.get(i).toLowerCase()
						+ ".id=0\" target=\"navTab\" rel=\""
						+ clist.get(i).toLowerCase()
						+ "list\"><tsp:transproname proname=\""
						+ clist.get(i).toLowerCase() + "menu\"/></a></li>", 1);
			}
		}
		append(content, "								</ul>", 1);
		append(content, "							</li>", 1);
		append(content, "						</ul>", 1);
		append(content, "					</div>", 1);
		append(content, "				</div>", 1);
		append(content, "			</div>", 1);
		append(content, "		</div>", 1);
		append(content, "		<div id=\"container\">", 1);
		append(content, "			<div id=\"navTab\" class=\"tabsPage\">", 1);
		append(content, "				<div class=\"tabsPageHeader\">", 1);
		append(
				content,
				"					<div class=\"tabsPageHeaderContent\"><!-- 显示左右控制时添加 class=\"tabsPageHeaderMargin\" -->",
				1);
		append(content, "						<ul class=\"navTab-tab\">", 1);
		append(
				content,
				"							<li tabid=\"main\" class=\"main\"><a href=\"javascript:;\"><span><span class=\"home_icon\">我的主页</span></span></a></li>",
				1);
		append(content, "						</ul>", 1);
		append(content, "					</div>", 1);
		append(
				content,
				"					<div class=\"tabsLeft\">left</div><!-- 禁用只需要添加一个样式 class=\"tabsLeft tabsLeftDisabled\" -->",
				1);
		append(
				content,
				"					<div class=\"tabsRight\">right</div><!-- 禁用只需要添加一个样式 class=\"tabsRight tabsRightDisabled\" -->",
				1);
		append(content, "					<div class=\"tabsMore\">more</div>", 1);
		append(content, "				</div>", 1);
		append(content, "				<ul class=\"tabsMoreList\">", 1);
		append(content, "					<li><a href=\"javascript:;\">我的主页</a></li>", 1);
		append(content, "				</ul>", 1);
		append(content,
				"				<div class=\"navTab-panel tabsPageContent layoutBox\">", 1);
		append(content, "					<div class=\"page unitBox\">", 1);
		append(content, "						<div class=\"accountInfo\">", 1);
		append(content, "						</div>", 1);
		append(
				content,
				"						<div class=\"pageFormContent\" layoutH=\"80\" style=\"margin-right:230px\">",
				1);
		append(content, "						</div>", 1);
		append(
				content,
				"						<div style=\"width:230px;position: absolute;top:60px;right:0\" layoutH=\"80\">",
				1);
		append(content, "						</div>", 1);
		append(content, "					</div>", 1);
		append(content, "					", 1);
		append(content, "				</div>", 1);
		append(content, "			</div>", 1);
		append(content, "		</div>", 1);
		append(content, "", 1);
		append(content, "	</div>", 1);
		append(content, "", 1);
		append(content, "<div id=\"footer\">" +  "管理系统</div>", 1);
		append(content, "", 1);
		append(content, "</body>", 1);
		append(content, "</html>", 1);

		bufferwriterToFile(content.toString(), indexpath);
	}

	public static void generateaction(List<String> clzzpo,
			HashSet<String> serviceset, String clazzname, String pkgname,
			String acname) {
		StringBuffer content = new StringBuffer();
		content.delete(0, content.length());
		content.append("package pfc.web;");
		content.append("\r\n");
		content.append("\r\n");
		content.append("\r\n");
		content.append("import java.util.ArrayList;");
		content.append("\r\n");
		content.append("import java.util.List;");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zptype = clzzpo.get(i).split("@")[1];
			if (zptype.equals("set")) {
				append(content, "import java.util.HashSet;", 1);
				break;
			}
		}
		content.append("import java.util.StringTokenizer;");
		content.append("\r\n");
		for (String sic : serviceset) {
			append(content, "import pfc.bean." + sic + ";", 1);
			append(content, "import pfc.service." + sic.toLowerCase() + "."
					+ sic + "Service;", 1);
		}
		content.append("import pfc.bean." + clazzname + ";");
		content.append("\r\n");
		content.append("import pfc.service." + pkgname + "." + clazzname
				+ "Service;");
		content.append("\r\n");
		content.append("\r\n");
		content.append("public class " + clazzname
				+ "Action extends BaseAction {");
		content.append("\r\n");
		content.append("\r\n");
		content.append("	private " + clazzname + "Service " + pkgname
				+ "Service;");
		content.append("\r\n");
		content.append("	private List<" + clazzname + "> " + pkgname + "list;");
		content.append("\r\n");
		content.append("	private " + clazzname + " " + pkgname + ";");
		content.append("\r\n");
		content.append("	private String " + pkgname + "ids;");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "	private " + cxname + " " + zpname + ";", 1);
			} else if (zptype.equals("set")) {
				append(content, "	private " + cxname + " " + zpname + ";", 1);
			}
		}
		for (String sic : serviceset) {
			append(content, "	private " + sic + "Service " + sic.toLowerCase()
					+ "Service;", 1);
		}
		content.append("\r\n");
		content.append("	public String list(){");
		content.append("\r\n");
		append(content, "		if(" + pkgname + "!=null&&" + pkgname
				+ ".getId()!=null&&" + pkgname + ".getId()==0){", 1);
		append(content, "			" + pkgname + "=null;", 1);
		append(content, "		}", 1);
		append(content, "		" + pkgname + "list=" + pkgname
				+ "Service.findByObject(" + pkgname + ");", 1);
		append(content, "		if(" + pkgname + "list==null){", 1);
		content
				.append("			pager="
						+ pkgname
						+ "Service.findPage(pageNum, numPerPage, orderField, orderDirection);");
		content.append("\r\n");
		content.append("			" + pkgname + "list=pager.getList();");
		content.append("\r\n");
		content.append("			super.page();");
		content.append("\r\n");
		append(content, "		}", 1);
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String listlook(){");
		content.append("\r\n");
		append(content, "		" + pkgname + "list=" + pkgname
				+ "Service.findByObject(" + pkgname + ");", 1);
		append(content, "		if(" + pkgname + "list==null){", 1);
		content
				.append("			pager="
						+ pkgname
						+ "Service.findPage(pageNum, numPerPage, orderField, orderDirection);");
		content.append("\r\n");
		content.append("			" + pkgname + "list=pager.getList();");
		content.append("\r\n");
		content.append("			super.page();");
		content.append("\r\n");
		append(content, "		}", 1);
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String listlooks(){");
		content.append("\r\n");
		append(content, "		" + pkgname + "list=" + pkgname
				+ "Service.findByObject(" + pkgname + ");", 1);
		append(content, "		if(" + pkgname + "list==null){", 1);
		append(content, "			" + pkgname + "list=" + pkgname
				+ "Service.getAll();", 1);
		append(content, "		}", 1);
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String insertUI(){");
		content.append("\r\n");
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void insert(){");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "		if (" + zpname + " != null && " + zpname
						+ ".getId() != null) {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(" + cxname.toLowerCase() + "Service.find(" + zpname
						+ ".getId()));", 1);
				append(content, "		} else {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(null);", 1);
				append(content, "		}", 1);
			} else if (zptype.equals("set")) {
				append(content, "		if (" + zpname + " != null && !" + zpname
						+ ".getIds().equals(\"\")) {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(new HashSet(" + cxname.toLowerCase()
						+ "Service.findByIds(" + zpname + ".getIds())));", 1);
				append(content, "		} else {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(null);", 1);
				append(content, "		}", 1);
			}
		}
		content.append("		" + pkgname + "Service.save(" + pkgname + ");");
		content.append("\r\n");
		content.append("		super.returnjson(\"" + pkgname + "list\");");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String updateUI(){");
		content.append("\r\n");
		content.append("		if(" + pkgname + ".getId()!=null){");
		content.append("\r\n");
		content.append("			" + pkgname + "=" + pkgname + "Service.find("
				+ pkgname + ".getId());");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "			" + zpname + "=" + pkgname + ".get"
						+ returnUstr(zpname) + "();", 1);
			} else if (zptype.equals("set")) {
				append(content, "			ids=\"\";names=\"\";", 1);
				append(content, "			for (Object obj: " + pkgname + ".get"
						+ returnUstr(zpname) + "()) {  ", 1);
				append(content, "				if(obj instanceof " + cxname + "){ ", 1);
				append(content, "					" + cxname + " oj=(" + cxname + ") obj;",
						1);
				append(content, "					ids+=oj.getId()+\",\";", 1);
				append(content, "					names+=oj.get"
						+ returnUstr(getDisplayname(cxname)) + "()+\",\";", 1);
				append(content, "				}", 1);
				append(content, "			}", 1);
				append(
						content,
						"			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}",
						1);
				append(
						content,
						"			if(names.length()>0){names=names.substring(0, names.length()-1);}",
						1);
				append(content, "			" + zpname + "=new " + cxname + "();", 1);
				append(content, "			" + zpname + ".setIds(ids);", 1);
				append(content, "			" + zpname + ".setNames(names);", 1);
			}
		}
		content.append("		}");
		content.append("\r\n");
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void update(){");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "		if (" + zpname + " != null && " + zpname
						+ ".getId() != null) {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(" + cxname.toLowerCase() + "Service.find(" + zpname
						+ ".getId()));", 1);
				append(content, "		} else {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(null);", 1);
				append(content, "		}", 1);
			} else if (zptype.equals("set")) {
				append(content, "		if (" + zpname + " != null && !" + zpname
						+ ".getIds().equals(\"\")) {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(new HashSet(" + cxname.toLowerCase()
						+ "Service.findByIds(" + zpname + ".getIds())));", 1);
				append(content, "		} else {", 1);
				append(content, "			" + pkgname + ".set" + returnUstr(zpname)
						+ "(null);", 1);
				append(content, "		}", 1);
			}
		}
		content.append("		" + pkgname + "Service.update(" + pkgname + ");");
		content.append("\r\n");
		content.append("		super.returnjson(\"" + pkgname + "list\");");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String view(){");
		content.append("\r\n");
		content.append("		if(" + pkgname + ".getId()!=null){");
		content.append("\r\n");
		content.append("			" + pkgname + "=" + pkgname + "Service.find("
				+ pkgname + ".getId());");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "			" + zpname + "=" + pkgname + ".get"
						+ returnUstr(zpname) + "();", 1);
			} else if (zptype.equals("set")) {
				append(content, "			ids=\"\";names=\"\";", 1);
				append(content, "			for (Object obj: " + pkgname + ".get"
						+ returnUstr(zpname) + "()) {  ", 1);
				append(content, "				if(obj instanceof " + cxname + "){ ", 1);
				append(content, "					" + cxname + " oj=(" + cxname + ") obj;",
						1);
				append(content, "					ids+=oj.getId()+\",\";", 1);
				append(content, "					names+=oj.get"
						+ returnUstr(getDisplayname(cxname)) + "()+\",\";", 1);
				append(content, "				}", 1);
				append(content, "			}", 1);
				append(
						content,
						"			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}",
						1);
				append(
						content,
						"			if(names.length()>0){names=names.substring(0, names.length()-1);}",
						1);
				append(content, "			" + zpname + "=new " + cxname + "();", 1);
				append(content, "			" + zpname + ".setIds(ids);", 1);
				append(content, "			" + zpname + ".setNames(names);", 1);
			}
		}
		content.append("		}");
		content.append("\r\n");
		content.append("		return SUCCESS;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void del(){");
		content.append("\r\n");
		content.append("		StringTokenizer stk=new StringTokenizer(" + pkgname
				+ "ids, \",\");");
		content.append("\r\n");
		content.append("		List<Integer> lds=new ArrayList<Integer>();");
		content.append("\r\n");
		content.append("		while(stk.hasMoreElements()){");
		content.append("\r\n");
		content
				.append("			lds.add(Integer.valueOf(stk.nextElement().toString()));");
		content.append("\r\n");
		content.append("		}");
		content.append("\r\n");
		content.append("		" + pkgname + "Service.deleteByIds(lds.toArray());");
		content.append("\r\n");
		content.append("		super.returnjsondel(\"" + pkgname + "list\");");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public " + clazzname + "Service get" + clazzname
				+ "Service() {");
		content.append("\r\n");
		content.append("		return " + pkgname + "Service;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void set" + clazzname + "Service(" + clazzname
				+ "Service " + pkgname + "Service) {");
		content.append("\r\n");
		content.append("		this." + pkgname + "Service = " + pkgname
				+ "Service;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public List<" + clazzname + "> get" + clazzname
				+ "list() {");
		content.append("\r\n");
		content.append("		return " + pkgname + "list;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void set" + clazzname + "list(List<"
				+ clazzname + "> " + pkgname + "list) {");
		content.append("\r\n");
		content.append("		this." + pkgname + "list = " + pkgname + "list;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public " + clazzname + " get" + clazzname + "() {");
		content.append("\r\n");
		content.append("		return " + pkgname + ";");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void set" + clazzname + "(" + clazzname + " "
				+ pkgname + ") {");
		content.append("\r\n");
		content.append("		this." + pkgname + " = " + pkgname + ";");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public String get" + clazzname + "ids() {");
		content.append("\r\n");
		content.append("		return " + pkgname + "ids;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");

		content.append("	public void set" + clazzname + "ids(String " + pkgname
				+ "ids) {");
		content.append("\r\n");
		content.append("		this." + pkgname + "ids = " + pkgname + "ids;");
		content.append("\r\n");
		content.append("	}");
		content.append("\r\n");
		content.append("\r\n");
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, generateSetGet(cxname, zpname), 1);
			} else if (zptype.equals("set")) {
				append(content, generateSetGet(cxname, zpname), 1);
			}
		}
		for (String sic : serviceset) {
			append(content, generateSetGet(sic + "Service", sic.toLowerCase()
					+ "Service"), 1);
		}
		content.append("\r\n");
		content.append("\r\n");
		content.append("}");
		bufferwriterToFile(content.toString(), acname);
	}

	public static void generatepagequery(String pkgname, List<String> clzzp,
			String pagequery) {
		StringBuffer content = new StringBuffer();
		// 生成query页面
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(
				content,
				"	<form method=\"post\" action=\""
						+ pkgname
						+ "_list.do\" class=\"pageForm\" onsubmit=\"return navTabSearch(this);\">",
				1);
		append(content, "		<div class=\"pageFormContent\" layoutH=\"58\">", 1);

		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			if (zptype.equals("String")) {
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + pkgname + "." + zpname
						+ "\" type=\"text\" maxlength=\"150\" />", 1);
				append(content, "			</div>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<tsp:select name=\"" + pkgname + "."
						+ zpname + "\" />", 1);
				append(content, "			</div>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>起：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "Start\" id=\""
								+ zpname
								+ "Start\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({maxDate:'#F{$dp.$D(\\'"
								+ zpname + "End\\')||\\'2120-10-01\\'}'})\" />",
						1);
				append(content, "			</div>", 1);
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>止：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "End\" id=\""
								+ zpname
								+ "End\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({minDate:'#F{$dp.$D(\\'"
								+ zpname + "Start\\')}'})\"/>", 1);
				append(content, "			</div>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>起：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "Start\" id=\""
								+ zpname
								+ "Start\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({maxDate:'#F{$dp.$D(\\'"
								+ zpname
								+ "End\\')||\\'2120-10-01\\'}',dateFmt:'yyyy-MM-dd HH:mm:ss'})\" />",
						1);
				append(content, "			</div>", 1);
				append(content, "			<div class=\"unit\">", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>止：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "End\" id=\""
								+ zpname
								+ "End\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({minDate:'#F{$dp.$D(\\'"
								+ zpname
								+ "Start\\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})\"/>",
						1);
				append(content, "			</div>", 1);
			}

		}
		append(content, "		</div>", 1);
		append(content, "		<div class=\"formBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">开始检索</button></div></div></li>",
				1);
		append(
				content,
				"				<li><div class=\"button\"><div class=\"buttonContent\"><button type=\"reset\">清空重输</button></div></div></li>",
				1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		bufferwriterToFile(content.toString(), pagequery);
	}

	public static void generatepageview(String pkgname, List<String> clzzp,
			List<String> clzzpo, String pageview) {
		StringBuffer content = new StringBuffer();
		// 生成view页面
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(
				content,
				"	<form method=\"post\" action=\""
						+ "/per_insertOrUpdate.htm\" class=\"pageForm required-validate\" onsubmit=\"return validateCallback(this, navTabAjaxDone);\">",
				1);
		append(content, "		<div class=\"pageFormContent\" layoutH=\"56\">", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			append(content, "			<p>", 1);
			if (zptype.equals("String")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				${" + pkgname + "." + zpname + "}", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<tsp:selectview name=\"" + pkgname + "."
						+ zpname + "\" value=\"${" + pkgname + "." + zpname
						+ "}\"/>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<td> <fmt:formatDate value=\"${" + pkgname
						+ "." + zpname + "}\" pattern=\"yyyy-MM-dd\"/></td>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<td> <fmt:formatDate value=\"${" + pkgname
						+ "." + zpname
						+ "}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>", 1);
			}
			append(content, "			</p>", 1);
		}
		append(content, "			<div class=\"divider\"></div>", 1);
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".id\" value=\"${" + zpname
						+ ".id}\" type=\"hidden\"/>", 1);
				append(content, "				${" + zpname + "."
						+ getDisplayname(cxname) + "}", 1);
			} else if (zptype.equals("set")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".ids\" value=\"${" + zpname
						+ ".ids}\" type=\"hidden\"/>", 1);
				append(content, "				${" + zpname + ".names}", 1);
			}
			append(content, "			</p>", 1);
		}
		append(content, "		</div>", 1);
		append(content, "		<div class=\"formBar\">", 1);
		append(content, "			<ul>", 1);
		append(content, "				<li>", 1);
		append(
				content,
				"					<div class=\"button\"><div class=\"buttonContent\"><button type=\"button\" class=\"close\">返回</button></div></div>",
				1);
		append(content, "				</li>", 1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		bufferwriterToFile(content.toString(), pageview);
	}

	public static void generatepageupdate(String pkgname, List<String> clzzp,
			List<String> clzzpo, String pageupdateUI) {
		StringBuffer content = new StringBuffer();
		// 生成update页面
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(
				content,
				"	<form method=\"post\" action=\""
						+ pkgname
						+ "_update.do\" class=\"pageForm required-validate\" onsubmit=\"return validateCallback(this, navTabAjaxDone);\">",
				1);
		append(content, "		<div class=\"pageFormContent\" layoutH=\"56\">", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			append(content, "			<p>", 1);
			if (zptype.equals("String")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + pkgname + "." + zpname
						+ "\" type=\"text\" maxlength=\"150\" value=\"${"
						+ pkgname + "." + zpname + " }\"/>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<tsp:select name=\"" + pkgname + "."
						+ zpname + "\" value=\"${" + pkgname + "." + zpname
						+ "}\"/>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd})\" value=\"${"
								+ pkgname + "." + zpname + "}\"/>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" value=\"${"
								+ pkgname + "." + zpname + "}\"/>", 1);
			}
			append(content, "			</p>", 1);
		}
		append(content, "			<div class=\"divider\"></div>", 1);
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".id\" value=\"${" + zpname
						+ ".id}\" type=\"hidden\"/>", 1);
				append(content, "				<input name=\"" + zpname + "."
						+ getDisplayname(cxname) + "\" value=\"${" + zpname
						+ "." + getDisplayname(cxname) + "}\" type=\"text\"/>",
						1);
				append(content, "				<a class=\"btnLook\" href=\"" + cxname.toLowerCase()
						+ "_listlook.do\" lookupGroup=\"" + zpname
						+ "\">查找带回</a>	", 1);
			} else if (zptype.equals("set")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".ids\" value=\"${" + zpname
						+ ".ids}\" type=\"hidden\"/>", 1);
				append(content, "				<input name=\"" + zpname + "."
						+ getDisplayname(cxname) + "\" value=\"${" + zpname
						+ ".names}\" type=\"text\"/>", 1);
				append(content, "				<a class=\"btnLook\" href=\"" + cxname.toLowerCase()
						+ "_listlooks.do\" lookupGroup=\"" + zpname
						+ "\">查找带回</a>	", 1);
			}
			append(content, "			</p>", 1);
		}
		append(content, "		</div>", 1);
		append(content, "		<div class=\"formBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">保存</button></div></div></li>",
				1);
		append(content, "				<li>", 1);
		append(
				content,
				"					<div class=\"button\"><div class=\"buttonContent\"><button type=\"button\" class=\"close\">取消</button></div></div>",
				1);
		append(content, "				</li>", 1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);

		bufferwriterToFile(content.toString(), pageupdateUI);
	}

	public static void generatepageinsert(String pkgname, List<String> clzzp,
			List<String> clzzpo, String pageinserui) {
		StringBuffer content = new StringBuffer();
		// 生成insert页面
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(
				content,
				"	<form method=\"post\" action=\""
						+ pkgname
						+ "_insert.do\" class=\"pageForm required-validate\" onsubmit=\"return validateCallback(this, navTabAjaxDone);\">",
				1);
		append(content, "		<div class=\"pageFormContent\" layoutH=\"56\">", 1);
		append(content, "				<input name=\"" + pkgname
				+ ".id\" type=\"hidden\"/>", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			append(content, "			<p>", 1);
			if (zptype.equals("String")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + pkgname + "." + zpname
						+ "\" type=\"text\" maxlength=\"150\" value=\"\"/>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<tsp:select name=\"" + pkgname + "."
						+ zpname + "\"/>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker()\" value=\"\"/>",
						1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(
						content,
						"				<input class=\"Wdate\" name=\""
								+ pkgname
								+ "."
								+ zpname
								+ "\" type=\"text\" maxlength=\"150\" onFocus=\"WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})\" value=\"\"/>",
						1);
			}
			append(content, "			</p>", 1);
		}

		append(content, "			<div class=\"divider\"></div>", 1);
		for (int i = 0; i < clzzpo.size(); i++) {
			String zpname = clzzpo.get(i).split("@")[0];
			String zptype = clzzpo.get(i).split("@")[1];
			String cxname = clzzpo.get(i).split("@")[2];
			if (zptype.equals("one")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".id\" type=\"hidden\"/>", 1);
				append(content, "				<input name=\"" + zpname + "."
						+ getDisplayname(cxname) + "\" type=\"text\"/>", 1);
				append(content, "				<a class=\"btnLook\" href=\"" + cxname.toLowerCase()
						+ "_listlook.do\" lookupGroup=\"" + zpname
						+ "\">查找带回</a>	", 1);
			} else if (zptype.equals("set")) {
				append(content, "			<p>", 1);
				append(content, "				<label><tsp:transproname proname=\""
						+ pkgname + "." + zpname + "\"/>：</label>", 1);
				append(content, "				<input name=\"" + zpname
						+ ".ids\" type=\"hidden\"/>", 1);
				append(content, "				<input name=\"" + zpname + "."
						+ getDisplayname(cxname) + "\" type=\"text\"/>", 1);
				append(content, "				<a class=\"btnLook\" href=\"" + cxname.toLowerCase()
						+ "_listlooks.do\" lookupGroup=\"" + zpname
						+ "\">查找带回</a>	", 1);
			}
			append(content, "			</p>", 1);
		}
		append(content, "		</div>", 1);
		append(content, "		<div class=\"formBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">保存</button></div></div></li>",
				1);
		append(content, "				<li>", 1);
		append(
				content,
				"					<div class=\"button\"><div class=\"buttonContent\"><button type=\"button\" class=\"close\">取消</button></div></div>",
				1);
		append(content, "				</li>", 1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		bufferwriterToFile(content.toString(), pageinserui);

	}

	public static void generatepagelistlooks(String pkgname,
			List<String> clzzp, String pagelistlooks) {
		StringBuffer content = new StringBuffer();
		// 生成listlooks页面========加复选框
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<form id=\"pagerForm\" method=\"post\" action=\"" + pkgname + "_listlooks.do\">", 1);
		append(content,
				"	<input type=\"hidden\" name=\"pageNum\" value=\"1\" />", 1);
		append(
				content,
				"	<input type=\"hidden\" name=\"numPerPage\" value=\"${numPerPage}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderField\" value=\"${orderField}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderDirection\" value=\"${orderDirection}\" />",
				1);
		append(content, "</form>", 1);
		append(content, "", 1);
		append(content, "<div class=\"pageHeader\">", 1);
		append(
				content,
				"	<form rel=\"pagerForm\" onsubmit=\"return dwzSearch(this, 'dialog');\" action=\""
						+ pkgname
						+ "_listlooks.do\" method=\"post\">", 1);
		append(content, "	<div class=\"searchBar\">", 1);
		append(content, "		<ul class=\"searchContent\">", 1);
		append(content, "			<li>", 1);
		append(content, "				<label><tsp:transproname proname=\"" + pkgname
				+ "." + getDisplayname(returnUstr(pkgname)) + "\"/>：</label>",
				1);
		append(content, "				<input type=\"text\" name=\"" + pkgname + "."
				+ getDisplayname(returnUstr(pkgname)) + "\" value=\"\"/>", 1);
		append(content, "			</li>", 1);
		append(content, "		</ul>", 1);
		append(content, "		<div class=\"subBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">检索</button></div></div></li>",
				1);
		append(content,
				"				<li><div class=\"button\"><div class=\"buttonContent\">"
						+ "<button type=\"button\" multLookup=\"" + pkgname
						+ "\" warn=\"请选择\">选择带回</button></div></div></li>", 1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(content, "	<div layoutH=\"116\" id=\"w_list_print\">", 1);
		append(
				content,
				"	<table class=\"table\" width=\"98%\" targetType=\"dialog\" asc=\"asc\" desc=\"desc\">",
				1);
		append(content, "		<thead>", 1);
		append(content, "			<tr>", 1);
		append(content,
				"			    <th width=\"22\"><input type=\"checkbox\" group=\""
						+ pkgname + "\" class=\"checkboxCtrl\"></th>", 1);
		append(
				content,
				"				<th orderField=\"id\" <s:if test=\"orderField=='id'\">class=\"${orderDirection}\"</s:if> title=\"排序\">编号</th> ",
				1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			append(
					content,
					"				<th orderField=\""
							+ zpname
							+ "\" <s:if test=\"orderField=='"
							+ zpname
							+ "'\">class=\"${orderDirection}\"</s:if>><tsp:transproname proname=\""
							+ pkgname + "." + zpname + "\"/></th>", 1);
		}
		append(content, "			</tr>", 1);
		append(content, "		</thead>", 1);
		append(content, "		<tbody>", 1);
		append(content, "		<s:iterator id=\"pojo\" value=\"" + pkgname
				+ "list\">", 1);
		append(content, "			<tr target=\"sid_entity\" rel=\"${pojo.id}\">", 1);
		String tempstr = getDisplayname(returnUstr(pkgname));
		append(content, "			    <td><input name=\"" + pkgname
				+ "\" value=\"{ids:'${pojo.id}', " + tempstr + ":'${pojo."
				+ tempstr + "}'}\" type=\"checkbox\"></td>", 1);
		append(content, "				<td>${pojo.id}</td>", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			if (zptype.equals("String")) {
				append(content, "				<td>${pojo." + zpname + "}</td>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<td><tsp:selectview name=\"" + pkgname
						+ "." + zpname + "\" value=\"${pojo." + zpname
						+ "}\"/></td>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<td> <fmt:formatDate value=\"${pojo."
						+ zpname + "}\" pattern=\"yyyy-MM-dd\"/></td>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content,
						"				<td> <fmt:formatDate value=\"${pojo." + zpname
								+ "}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>",
						1);
			}

		}
		append(content, "			</tr>", 1);
		append(content, "		 </s:iterator>", 1);
		append(content, "		</tbody>", 1);
		append(content, "	</table>", 1);
		append(content, "	</div>", 1);
		append(content, "</div>", 1);
		append(content, "", 1);
		bufferwriterToFile(content.toString(), pagelistlooks);

	}

	public static void generatepagelistlook(String pkgname, List<String> clzzp,
			String pagelistlook) {
		StringBuffer content = new StringBuffer();
		// 生成listlook页面
		content.delete(0, content.length());
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<form id=\"pagerForm\" method=\"post\" action=\"" + pkgname + "_listlook.do\">", 1);
		append(content,
				"	<input type=\"hidden\" name=\"pageNum\" value=\"1\" />", 1);
		append(
				content,
				"	<input type=\"hidden\" name=\"numPerPage\" value=\"${numPerPage}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderField\" value=\"${orderField}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderDirection\" value=\"${orderDirection}\" />",
				1);
		append(content, "</form>", 1);
		append(content, "", 1);
		append(content, "<div class=\"pageHeader\">", 1);
		append(
				content,
				"	<form rel=\"pagerForm\" onsubmit=\"return dwzSearch(this, 'dialog');\" action=\""
						+ pkgname
						+ "_listlook.do\" method=\"post\">", 1);
		append(content, "	<div class=\"searchBar\">", 1);
		append(content, "		<ul class=\"searchContent\">", 1);
		append(content, "			<li>", 1);
		append(content, "				<label><tsp:transproname proname=\"" + pkgname
				+ "." + getDisplayname(returnUstr(pkgname)) + "\"/>：</label>",
				1);
		append(content, "				<input type=\"text\" name=\"" + pkgname + "."
				+ getDisplayname(returnUstr(pkgname)) + "\" value=\"\"/>", 1);
		append(content, "			</li>", 1);
		append(content, "		</ul>", 1);
		append(content, "		<div class=\"subBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">检索</button></div></div></li>",
				1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(content, "	<div layoutH=\"116\" id=\"w_list_print\">", 1);
		append(
				content,
				"	<table class=\"table\" width=\"98%\" targetType=\"dialog\" asc=\"asc\" desc=\"desc\">",
				1);
		append(content, "		<thead>", 1);
		append(content, "			<tr>", 1);
		append(
				content,
				"				<th orderField=\"id\" <s:if test=\"orderField=='id'\">class=\"${orderDirection}\"</s:if> title=\"排序\">编号</th> ",
				1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			append(
					content,
					"				<th orderField=\""
							+ zpname
							+ "\" <s:if test=\"orderField=='"
							+ zpname
							+ "'\">class=\"${orderDirection}\"</s:if>><tsp:transproname proname=\""
							+ pkgname + "." + zpname + "\"/></th>", 1);
		}
		append(content, "			    <th>查找带回</th>", 1);
		append(content, "			</tr>", 1);
		append(content, "		</thead>", 1);
		append(content, "		<tbody>", 1);
		append(content, "		<s:iterator id=\"pojo\" value=\"" + pkgname
				+ "list\">", 1);
		append(content, "			<tr target=\"sid_entity\" rel=\"${pojo.id}\">", 1);
		append(content, "				<td>${pojo.id}</td>", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			if (zptype.equals("String")) {
				append(content, "				<td>${pojo." + zpname + "}</td>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<td><tsp:selectview name=\"" + pkgname
						+ "." + zpname + "\" value=\"${pojo." + zpname
						+ "}\"/></td>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<td> <fmt:formatDate value=\"${pojo."
						+ zpname + "}\" pattern=\"yyyy-MM-dd\"/></td>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content,
						"				<td> <fmt:formatDate value=\"${pojo." + zpname
								+ "}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>",
						1);
			}

		}
		append(content, "				<td>", 1);
		String tempstr = getDisplayname(returnUstr(pkgname));
		append(
				content,
				"			<a class=\"btnSelect\" href=\"javascript:$.bringBack({id:'${pojo.id }',"
						+ tempstr
						+ ":'${pojo."
						+ tempstr
						+ "}'})\" multLookup=\"empItems\" title=\"查找带回\">选择</a>",
				1);
		append(content, "				</td>", 1);
		append(content, "			</tr>", 1);
		append(content, "		 </s:iterator>", 1);
		append(content, "		</tbody>", 1);
		append(content, "	</table>", 1);
		append(content, "	</div>", 1);
		append(content, "	<div class=\"panelBar\" >", 1);
		append(content, "		<div class=\"pages\">", 1);
		append(content, "			<span>显示</span>", 1);
		append(
				content,
				"			<select class=\"combox\" name=\"numPerPage\" onchange=\"dialogPageBreak({numPerPage:this.value})\">",
				1);
		append(
				content,
				"			<option <s:if test=\"numPerPage==5\">selected=\"selected\"</s:if> value=\"5\" >5</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==10\">selected=\"selected\"</s:if> value=\"10\" >10</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==20\">selected=\"selected\"</s:if> value=\"20\" >20</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==50\">selected=\"selected\"</s:if> value=\"50\">50</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==100\">selected=\"selected\"</s:if> value=\"100\">100</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==200\">selected=\"selected\"</s:if> value=\"200\">200</option>",
				1);
		append(content, "			</select>", 1);
		append(content, "			<span>条，共${totalCount}条</span>", 1);
		append(content, "		</div>", 1);
		append(content, "		", 1);
		append(
				content,
				"		<div class=\"pagination\" targetType=\"dialog\" totalCount=\"${totalCount}\" numPerPage=\"${numPerPage}\" pageNumShown=\"10\" currentPage=\"${pageNum}\"></div>",
				1);
		append(content, "", 1);
		append(content, "	</div>", 1);
		append(content, "</div>", 1);
		append(content, "", 1);
		bufferwriterToFile(content.toString(), pagelistlook);
	}

	public static void generatepagelist(String pkgname, List<String> clzzp,
			String pagelist) {
		StringBuffer content = new StringBuffer();
		// 生成list页面
		append(content, "﻿<%@ page contentType=\"text/html; charset=UTF-8\"%>",
				1);
		append(content, "<%@ page pageEncoding=\"UTF-8\" %>", 1);
		append(content, "<%@include file=\"/pages/common/base.jsp\" %>", 1);
		append(content, "<base href=\"<%=basePath%>\">", 1);
		append(content, "<form id=\"pagerForm\" method=\"post\" action=\"" + pkgname + "_list.do\">", 1);
		append(content,
				"	<input type=\"hidden\" name=\"pageNum\" value=\"1\" />", 1);
		append(
				content,
				"	<input type=\"hidden\" name=\"numPerPage\" value=\"${numPerPage}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderField\" value=\"${orderField}\" />",
				1);
		append(
				content,
				"	<input type=\"hidden\" name=\"orderDirection\" value=\"${orderDirection}\" />",
				1);
		append(content, "</form>", 1);
		append(content, "", 1);
		append(content, "<div class=\"pageHeader\">", 1);
		append(content,
				"	<form rel=\"pagerForm\" onsubmit=\"return navTabSearch(this);\" action=\"" + pkgname
						+ "_list.do\" method=\"post\">", 1);
		append(content, "	<div class=\"searchBar\">", 1);
		append(content, "		<ul class=\"searchContent\">", 1);
		append(content, "			<li>", 1);
		append(content, "				<label><tsp:transproname proname=\"" + pkgname
				+ "." + getDisplayname(returnUstr(pkgname)) + "\"/>：</label>",
				1);
		append(content, "				<input type=\"text\" name=\"" + pkgname + "."
				+ getDisplayname(returnUstr(pkgname)) + "\" value=\"\"/>", 1);
		append(content, "			</li>", 1);
		append(content, "		</ul>", 1);
		append(content, "		<div class=\"subBar\">", 1);
		append(content, "			<ul>", 1);
		append(
				content,
				"				<li><div class=\"buttonActive\"><div class=\"buttonContent\"><button type=\"submit\">检索</button></div></div></li>",
				1);
		append(
				content,
				"				<li><a class=\"button\" href=\""
						+ "pages/"
						+ pkgname
						+ "/"
						+ pkgname
						+ "query.jsp\" target=\"dialog\" mask=\"true\" title=\"查询框\"><span>高级检索</span></a></li>",
				1);
		append(content, "			</ul>", 1);
		append(content, "		</div>", 1);
		append(content, "	</div>", 1);
		append(content, "	</form>", 1);
		append(content, "</div>", 1);
		append(content, "<div class=\"pageContent\">", 1);
		append(content, "	<div class=\"panelBar\">", 1);
		append(content, "		<ul class=\"toolBar\">", 1);
		append(content, "			<li><a class=\"add\" href=\"" + pkgname
				+ "_insertUI.do\" target=\"navTab\"><span>添加</span></a></li>",
				1);
		append(
				content,
				"			<li><a title=\"确实要删除这些记录吗?\" target=\"selectedTodo\" rel=\""
						+ pkgname + "ids\" postType=\"string\" href=\"" + pkgname
						+ "_del.do\" class=\"delete\"><span>删除</span></a></li>",
				1);
		append(
				content,
				"			<li><a class=\"edit\" href=\""+ pkgname
						+ "_updateUI.do?"
						+ pkgname
						+ ".id={sid_entity}\" target=\"navTab\" warn=\"请选择一条\"><span>修改</span></a></li>",
				1);
		append(
				content,
				"			<li><a class=\"edit\" href=\""+ pkgname
						+ "_view.do?"
						+ pkgname
						+ ".id={sid_entity}\" target=\"navTab\" warn=\"请选择一条\"><span>查看</span></a></li>",
				1);
		append(content, "			<li class=\"line\">line</li>", 1);
		append(content, "		</ul>", 1);
		append(content, "	</div>", 1);
		append(content, "	<div layoutH=\"116\" id=\"w_list_print\">", 1);
		append(
				content,
				"	<table class=\"table\" width=\"98%\" targetType=\"navTab\" asc=\"asc\" desc=\"desc\">",
				1);
		append(content, "		<thead>", 1);
		append(content, "			<tr>", 1);
		append(content,
				"			    <th width=\"22\"><input type=\"checkbox\" group=\""
						+ pkgname + "ids\" class=\"checkboxCtrl\"></th>", 1);
		append(
				content,
				"				<th orderField=\"id\" <s:if test=\"orderField=='id'\">class=\"${orderDirection}\"</s:if> title=\"排序\">编号</th> ",
				1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			append(
					content,
					"				<th orderField=\""
							+ zpname
							+ "\" <s:if test=\"orderField=='"
							+ zpname
							+ "'\">class=\"${orderDirection}\"</s:if>><tsp:transproname proname=\""
							+ pkgname + "." + zpname + "\"/></th>", 1);
		}
		append(content, "			    <th>操作</th>", 1);
		append(content, "			</tr>", 1);
		append(content, "		</thead>", 1);
		append(content, "		<tbody>", 1);
		append(content, "		<s:iterator id=\"pojo\" value=\"" + pkgname
				+ "list\">", 1);
		append(
				content,
				"			<tr target=\"sid_entity\" rel=\"${pojo.id}\" ondblclick=\"javascript:dbltable('"
						+ pkgname
						+ "_view.do?"
						+ pkgname
						+ ".id=${pojo.id}', 'pdialogid' ,'查看', 800, 600);\">",
				1);
		append(content, "			    <td><input name=\"" + pkgname
				+ "ids\" value=\"${pojo.id}\" type=\"checkbox\"></td>", 1);
		append(content, "				<td>${pojo.id}</td>", 1);
		for (int i = 0; i < clzzp.size(); i++) {
			String zpname = clzzp.get(i).split("@")[0];
			String zptype = clzzp.get(i).split("@")[1];
			if (zptype.equals("String")) {
				append(content, "				<td>${pojo." + zpname + "}</td>", 1);
			} else if (zptype.equals("Integer")) {
				append(content, "				<td><tsp:selectview name=\"" + pkgname
						+ "." + zpname + "\" value=\"${pojo." + zpname
						+ "}\"/></td>", 1);
			} else if (zptype.equals("Date")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content, "				<td> <fmt:formatDate value=\"${pojo."
						+ zpname + "}\" pattern=\"yyyy-MM-dd\"/></td>", 1);
			} else if (zptype.equals("Timestamp")
					&& !(zpname.contains("Start") || zpname.contains("End"))) {
				append(content,
						"				<td> <fmt:formatDate value=\"${pojo." + zpname
								+ "}\" pattern=\"yyyy-MM-dd HH:mm:ss\"/></td>",
						1);
			}

		}
		append(content, "				<td>", 1);
		append(content, "				    <a title=\"删除\" target=\"ajaxTodo\" href=\"" + pkgname + "_del.do?" + pkgname
				+ "ids=${pojo.id}\" class=\"btnDel\">删除</a>", 1);
		append(
				content,
				"					<a title=\"编辑\"  target=\"navTab\" mask=\"true\" href=\"" + pkgname + "_updateUI.do?"
						+ pkgname + ".id=${pojo.id}\" class=\"btnEdit\">编辑</a>",
				1);
		append(content,
				"					<a title=\"查看\"  target=\"navTab\" mask=\"true\" href=\"" + pkgname + "_view.do?" + pkgname
						+ ".id=${pojo.id}\">查看</a>", 1);
		append(content, "				</td>", 1);
		append(content, "			</tr>", 1);
		append(content, "		 </s:iterator>", 1);
		append(content, "		</tbody>", 1);
		append(content, "	</table>", 1);
		append(content, "	</div>", 1);
		append(content, "	<div class=\"panelBar\" >", 1);
		append(content, "		<div class=\"pages\">", 1);
		append(content, "			<span>显示</span>", 1);
		append(
				content,
				"			<select class=\"combox\" name=\"numPerPage\" onchange=\"navTabPageBreak({numPerPage:this.value})\">",
				1);
		append(
				content,
				"			<option <s:if test=\"numPerPage==5\">selected=\"selected\"</s:if> value=\"5\" >5</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==10\">selected=\"selected\"</s:if> value=\"10\" >10</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==20\">selected=\"selected\"</s:if> value=\"20\" >20</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==50\">selected=\"selected\"</s:if> value=\"50\">50</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==100\">selected=\"selected\"</s:if> value=\"100\">100</option>",
				1);
		append(
				content,
				"				<option <s:if test=\"numPerPage==200\">selected=\"selected\"</s:if> value=\"200\">200</option>",
				1);
		append(content, "			</select>", 1);
		append(content, "			<span>条，共${totalCount}条</span>", 1);
		append(content, "		</div>", 1);
		append(content, "		", 1);
		append(
				content,
				"		<div class=\"pagination\" targetType=\"${targetType}\" totalCount=\"${totalCount}\" numPerPage=\"${numPerPage}\" pageNumShown=\"10\" currentPage=\"${pageNum}\"></div>",
				1);
		append(content, "", 1);
		append(content, "	</div>", 1);
		append(content, "</div>", 1);
		append(content, "", 1);
		bufferwriterToFile(content.toString(), pagelist);

	}
}
