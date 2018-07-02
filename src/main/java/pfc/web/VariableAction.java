package pfc.web;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import pfc.bean.Variable;
import pfc.service.variable.VariableService;

public class VariableAction extends BaseAction {

	private VariableService variableService;
	private List<Variable> variablelist;
	private Variable variable;
	private String variableids;

	public String list(){
		if(variable!=null&&variable.getId()!=null&&variable.getId()==0){
			variable=null;
		}
		variablelist=variableService.findByObject(variable);
		if(variablelist==null){
			pager=variableService.findPage(pageNum, numPerPage, orderField, orderDirection);
			variablelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlook(){
		variablelist=variableService.findByObject(variable);
		if(variablelist==null){
			pager=variableService.findPage(pageNum, numPerPage, orderField, orderDirection);
			variablelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlooks(){
		variablelist=variableService.findByObject(variable);
		if(variablelist==null){
			variablelist=variableService.getAll();
		}
		return SUCCESS;
	}

	public String insertUI(){
		return SUCCESS;
	}

	public void insert(){
		variableService.save(variable);
		super.returnjson("variablelist");
	}

	public String updateUI(){
		if(variable.getId()!=null){
			variable=variableService.find(variable.getId());
		}
		return SUCCESS;
	}

	public void update(){
		variableService.update(variable);
		super.returnjson("variablelist");
	}

	public String view(){
		if(variable.getId()!=null){
			variable=variableService.find(variable.getId());
		}
		return SUCCESS;
	}

	public void del(){
		StringTokenizer stk=new StringTokenizer(variableids, ",");
		List<Integer> lds=new ArrayList<Integer>();
		while(stk.hasMoreElements()){
			lds.add(Integer.valueOf(stk.nextElement().toString()));
		}
		variableService.deleteByIds(lds.toArray());
		super.returnjsondel("variablelist");
	}

	public VariableService getVariableService() {
		return variableService;
	}

	public void setVariableService(VariableService variableService) {
		this.variableService = variableService;
	}

	public List<Variable> getVariablelist() {
		return variablelist;
	}

	public void setVariablelist(List<Variable> variablelist) {
		this.variablelist = variablelist;
	}

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public String getVariableids() {
		return variableids;
	}

	public void setVariableids(String variableids) {
		this.variableids = variableids;
	}



}