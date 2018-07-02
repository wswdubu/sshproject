package pfc.tag;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class TransPronameTag extends TagSupport {
	// 获得缺省的系统区域
	  Locale locale = Locale.getDefault();
	// 获得资源文件
	 ResourceBundle rb = ResourceBundle.getBundle("message", locale);
	 
	 private String proname;
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().write(returnI18n(proname));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return super.doStartTag();
	}
	
	public String getProname() {
		return proname;
	}


	public void setProname(String proname) {
		this.proname = proname;
	}


	public  String returnI18n(String str){
		String ss;
		try{
			ss=rb.getString(str);
		}catch (Exception e) {
			ss=str;
		}
		
		return ss;
	}
}
