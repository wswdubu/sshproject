package pfc.web;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import pfc.bean.Demo;
import pfc.service.demo.DemoService;

public class DemoAction extends BaseAction {

	private DemoService demoService;
	private List<Demo> demolist;
	private Demo demo;
	private String demoids;

	public String list(){
		if(demo!=null&&demo.getId()!=null&&demo.getId()==0){
			demo=null;
		}
		demolist=demoService.findByObject(demo);
		if(demolist==null){
			pager=demoService.findPage(pageNum, numPerPage, orderField, orderDirection);
			demolist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlook(){
		demolist=demoService.findByObject(demo);
		if(demolist==null){
			pager=demoService.findPage(pageNum, numPerPage, orderField, orderDirection);
			demolist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlooks(){
		demolist=demoService.findByObject(demo);
		if(demolist==null){
			demolist=demoService.getAll();
		}
		return SUCCESS;
	}

	public String insertUI(){
		return SUCCESS;
	}

	public void insert(){
		demoService.save(demo);
		super.returnjson("demolist");
	}

	public String updateUI(){
		if(demo.getId()!=null){
			demo=demoService.find(demo.getId());
		}
		return SUCCESS;
	}

	public void update(){
		demoService.update(demo);
		super.returnjson("demolist");
	}

	public String view(){
		if(demo.getId()!=null){
			demo=demoService.find(demo.getId());
		}
		return SUCCESS;
	}

	public void del(){
		StringTokenizer stk=new StringTokenizer(demoids, ",");
		List<Integer> lds=new ArrayList<Integer>();
		while(stk.hasMoreElements()){
			lds.add(Integer.valueOf(stk.nextElement().toString()));
		}
		demoService.deleteByIds(lds.toArray());
		super.returnjsondel("demolist");
	}

	public DemoService getDemoService() {
		return demoService;
	}

	public void setDemoService(DemoService demoService) {
		this.demoService = demoService;
	}

	public List<Demo> getDemolist() {
		return demolist;
	}

	public void setDemolist(List<Demo> demolist) {
		this.demolist = demolist;
	}

	public Demo getDemo() {
		return demo;
	}

	public void setDemo(Demo demo) {
		this.demo = demo;
	}

	public String getDemoids() {
		return demoids;
	}

	public void setDemoids(String demoids) {
		this.demoids = demoids;
	}



}