package pfc.tag;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import pfc.bean.Variable;
import pfc.service.variable.VariableService;
import pfc.util.SpringContextHolder;

public class SelectTag extends TagSupport {

	private static final long serialVersionUID = 189346748940696715L;
	private static VariableService variableService=SpringContextHolder.getBean("VariableService");
	private List<Variable> varlist;
	private String name;
	private String value;
	public int doStartTag() throws JspException {
		varlist=variableService.queryByPropertye("varname", name);
		if(value!=null&&!value.equals("")){
			try {
				StringBuffer selecttag=new StringBuffer();
				selecttag.append("<select name=\""+name+"\">").append("\r\n");
				selecttag.append("<option value=\"\">请选择</option>").append("\r\n");
				for (int i = 0; i < varlist.size(); i++) {
					if(varlist.get(i).getVarvalue().equals(value)){
						selecttag.append("	<option  selected=\"selected\" value=\""+varlist.get(i).getVarvalue()+"\"> "+varlist.get(i).getReturnname()+"</option>").append("\r\n");;
					}else{
						selecttag.append("	<option value=\""+varlist.get(i).getVarvalue()+"\">"+varlist.get(i).getReturnname()+"</option>").append("\r\n");;
					}
				}
				selecttag.append("</select>");
				pageContext.getOut().write(selecttag.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				StringBuffer selecttag=new StringBuffer();
				selecttag.append("<select name=\""+name+"\">").append("\r\n");
				selecttag.append("<option value=\"\">请选择</option>").append("\r\n");
				for (int i = 0; i < varlist.size(); i++) {
					selecttag.append("	<option value=\""+varlist.get(i).getVarvalue()+"\">"+varlist.get(i).getReturnname()+"</option>")
					.append("\r\n");
				}
				selecttag.append("</select>");
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
