package pfc.web;


import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.StringTokenizer;
import pfc.bean.Role;
import pfc.service.role.RoleService;
import pfc.bean.Privilege;
import pfc.service.privilege.PrivilegeService;

public class PrivilegeAction extends BaseAction {

	private PrivilegeService privilegeService;
	private List<Privilege> privilegelist;
	private Privilege privilege;
	private String privilegeids;
	private Role roles;
	private RoleService roleService;

	public String list(){
		if(privilege!=null&&privilege.getId()!=null&&privilege.getId()==0){
			privilege=null;
		}
		privilegelist=privilegeService.findByObject(privilege);
		if(privilegelist==null){
			pager=privilegeService.findPage(pageNum, numPerPage, orderField, orderDirection);
			privilegelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlook(){
		privilegelist=privilegeService.findByObject(privilege);
		if(privilegelist==null){
			pager=privilegeService.findPage(pageNum, numPerPage, orderField, orderDirection);
			privilegelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlooks(){
		privilegelist=privilegeService.findByObject(privilege);
		if(privilegelist==null){
			privilegelist=privilegeService.getAll();
		}
		return SUCCESS;
	}

	public String insertUI(){
		return SUCCESS;
	}

	public void insert(){
		if (roles != null && !roles.getIds().equals("")) {
			privilege.setRoles(new HashSet(roleService.findByIds(roles.getIds())));
		} else {
			privilege.setRoles(null);
		}
		privilegeService.save(privilege);
		super.returnjson("privilegelist");
	}

	public String updateUI(){
		if(privilege.getId()!=null){
			privilege=privilegeService.find(privilege.getId());
			ids="";names="";
			for (Object obj: privilege.getRoles()) {  
				if(obj instanceof Role){ 
					Role oj=(Role) obj;
					ids+=oj.getId()+",";
					names+=oj.getRolename()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			roles=new Role();
			roles.setIds(ids);
			roles.setNames(names);
		}
		return SUCCESS;
	}

	public void update(){
		if (roles != null && !roles.getIds().equals("")) {
			privilege.setRoles(new HashSet(roleService.findByIds(roles.getIds())));
		} else {
			privilege.setRoles(null);
		}
		privilegeService.update(privilege);
		super.returnjson("privilegelist");
	}

	public String view(){
		if(privilege.getId()!=null){
			privilege=privilegeService.find(privilege.getId());
			ids="";names="";
			for (Object obj: privilege.getRoles()) {  
				if(obj instanceof Role){ 
					Role oj=(Role) obj;
					ids+=oj.getId()+",";
					names+=oj.getRolename()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			roles=new Role();
			roles.setIds(ids);
			roles.setNames(names);
		}
		return SUCCESS;
	}

	public void del(){
		StringTokenizer stk=new StringTokenizer(privilegeids, ",");
		List<Integer> lds=new ArrayList<Integer>();
		while(stk.hasMoreElements()){
			lds.add(Integer.valueOf(stk.nextElement().toString()));
		}
		privilegeService.deleteByIds(lds.toArray());
		super.returnjsondel("privilegelist");
	}

	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}

	public List<Privilege> getPrivilegelist() {
		return privilegelist;
	}

	public void setPrivilegelist(List<Privilege> privilegelist) {
		this.privilegelist = privilegelist;
	}

	public Privilege getPrivilege() {
		return privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public String getPrivilegeids() {
		return privilegeids;
	}

	public void setPrivilegeids(String privilegeids) {
		this.privilegeids = privilegeids;
	}

	public Role getRoles() {
		return roles;
	}

	public void setRoles(Role roles) {
		this.roles = roles;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}



}