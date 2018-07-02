package pfc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ParseHiberXml {


	public static String getSetClazz(String xmlpath,String entityname){
		SAXBuilder builder = new SAXBuilder();
		builder = new SAXBuilder(false);
	      builder.setEntityResolver(new NoOpEntityResolver());
		InputStream file;
		try {
			file = new FileInputStream(xmlpath);
			Document document = builder.build(file);// 获得文档对象
			Element root = document.getRootElement();// 获得根节点
			List<Element> list = root.getChildren();
			for (Element e : list) {
				List<Element> eclist = e.getChildren();
				for (Element ec : eclist) {
					if(ec.getName().equals("set")&&ec.getAttributeValue("name").equals(entityname)){
						List<Element> xlist=ec.getChildren();
						for(Element x:xlist){
							if(x.getName().equals("many-to-many")){
								return x.getAttributeValue("entity-name").substring(x.getAttributeValue("entity-name").lastIndexOf(".")+1);
							}
						}
						
					}
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	public static void main(String[] args) {
		File directory = new File("");
		System.out.println(ParseHiberXml.getSetClazz(directory.getAbsolutePath()+"//src//pfc//bean//User.hbm.xml", "msgsForSender"));

	}
}
