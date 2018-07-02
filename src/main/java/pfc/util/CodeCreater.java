package pfc.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CodeCreater {
	
	public static void generater(List<String> pklist,boolean ifpri) throws Exception{
		for (int v = 0; v < pklist.size(); v++) {
		String clazzname=pklist.get(v);
		String pkgname=clazzname.toLowerCase();
		File directory = new File("");
	 	String servicepath=directory.getAbsolutePath()+"\\src\\main\\java\\pfc\\service\\"+pkgname;//要生成包的路径
	 	String beanpath=directory.getAbsolutePath()+"\\src\\main\\java\\pfc\\bean\\";//要生成bean的路径
	 	GenerateTool.generatetime(clazzname, beanpath);
	 	File file=new File(servicepath);
	 		String ifename=servicepath+"\\"+clazzname+"Service.java";
	 		List<String> clzzp=GenerateTool.getClazzPros(clazzname);
			List<String> clzzpo=GenerateTool.getClazzProsO(clazzname);
			HashSet<String> serviceset=GenerateTool.getClazzService(clazzname);
	 		file.mkdir();//新建service包
	 		//新建service接口
	 		
	 		file=new File(ifename);
	 		file.createNewFile();
	 		
			GenerateTool.generateservice(pkgname, clazzname, ifename);

			//新建impl包
			String implname=servicepath+"\\impl";
			file=new File(implname);
			file.mkdir();
			
			//新建ServiceImpl类
			String ifeimpl=implname+"\\"+clazzname+"ServiceImpl.java";
			file=new File(ifeimpl);
			file.createNewFile();
			
			GenerateTool.generateserviceimpl(pkgname, clazzname, ifeimpl);
			
			//新建Action类
			String actionpath=directory.getAbsolutePath()+"\\src\\main\\java\\pfc\\web\\";
			String acname=actionpath+clazzname+"Action.java";
			file=new File(acname);
			file.createNewFile();
			GenerateTool.generateaction(clzzpo, serviceset, clazzname, pkgname, acname);
			
			//生成spring bean
			String springpath=directory.getAbsolutePath()+"\\src\\main\\resources\\applicationContext-sys.xml";
			GenerateTool.addElement(springpath, pkgname, clazzname,serviceset);
			
			//生成pages页面
			String pagepath=directory.getAbsolutePath()+"\\src\\main\\webapp\\pages\\"+pkgname;
			file=new File(pagepath);
			file.mkdir();
			String pagelist=pagepath+"\\"+pkgname+"list.jsp";
			String pageinserui=pagepath+"\\"+pkgname+"insertUI.jsp";
			String pageupdateUI=pagepath+"\\"+pkgname+"updateUI.jsp";
			String pageview=pagepath+"\\"+pkgname+"view.jsp";
			String pagelistlook=pagepath+"\\"+pkgname+"listlook.jsp";
			String pagelistlooks=pagepath+"\\"+pkgname+"listlooks.jsp";
			String pagequery=pagepath+"\\"+pkgname+"query.jsp";
			String pagelogin=directory.getAbsolutePath()+"\\src\\main\\webapp\\"+"index.jsp";
			file=new File(pagelist);file.createNewFile();
			file=new File(pageinserui);file.createNewFile();
			file=new File(pageupdateUI);file.createNewFile();
			file=new File(pageview);file.createNewFile();
			file=new File(pagelistlook);file.createNewFile();
			file=new File(pagelistlooks);file.createNewFile();
			file=new File(pagequery);file.createNewFile();
			file=new File(pagelogin);file.createNewFile();
			GenerateTool.generatepagelist(pkgname, clzzp, pagelist);
			GenerateTool.generatepagelistlook(pkgname, clzzp, pagelistlook);
			GenerateTool.generatepagelistlooks(pkgname, clzzp, pagelistlooks);
			GenerateTool.generatepageinsert(pkgname, clzzp, clzzpo, pageinserui);
			GenerateTool.generatepageupdate(pkgname, clzzp, clzzpo, pageupdateUI);
			GenerateTool.generatepageview(pkgname, clzzp, clzzpo, pageview);
			GenerateTool.generatepagequery(pkgname, clzzp, pagequery);
			//GenerateTool.generatelogin(pagelogin);
			System.out.println(pkgname+"生成完毕");
		}
		if(ifpri){
			GenerateTool.generateprivilege(pklist);
		}
		
	}
	
	public static void main(String[] args) {
		try {
			List<String> pklist=new ArrayList<String>();
			pklist.add("Demo");
			CodeCreater.generater(pklist,false);
			List<String> menulist=new ArrayList<String>();
			//GenerateTool.generateindex(pklist,true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
