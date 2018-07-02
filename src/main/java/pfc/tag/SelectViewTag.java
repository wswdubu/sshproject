package pfc.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import pfc.bean.Variable;
import pfc.service.variable.VariableService;
import pfc.util.SpringContextHolder;

public class SelectViewTag extends TagSupport {
	private static final long serialVersionUID = 6281441946912565647L;
	private static VariableService variableService=SpringContextHolder.getBean("VariableService");
	private List<Variable> varlist;
	private String name;
	private String value;
	public int doStartTag() throws JspException {
		varlist=variableService.queryByPropertye("varname", name);
		if(value!=null&&!value.equals("")){
			try {
				StringBuffer selecttag=new StringBuffer();
				for (int i = 0; i < varlist.size(); i++) {
					if(varlist.get(i).getVarvalue().equals(value)){
						selecttag.append(varlist.get(i).getReturnname());
					}else{
						selecttag.append("");
					}
				}
				pageContext.getOut().write(selecttag.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				StringBuffer selecttag=new StringBuffer();
				for (int i = 0; i < varlist.size(); i++) {
					if(varlist.get(i).getVarvalue().equals(value)){
						selecttag.append(varlist.get(i).getReturnname());
					}else{
						selecttag.append("");
					}
				}
				pageContext.getOut().write(selecttag.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return super.doStartTag();
	}
	public List<Variable> getVarlist() {
		return varlist;
	}
	public void setVarlist(List<Variable> varlist) {
		this.varlist = varlist;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
