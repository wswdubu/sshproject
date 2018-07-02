package pfc.web;


import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.StringTokenizer;
import pfc.bean.User;
import pfc.service.user.UserService;
import pfc.bean.Privilege;
import pfc.service.privilege.PrivilegeService;
import pfc.bean.Role;
import pfc.service.role.RoleService;

public class RoleAction extends BaseAction {

	private RoleService roleService;
	private List<Role> rolelist;
	private Role role;
	private String roleids;
	private Privilege privileges;
	private User users;
	private UserService userService;
	private PrivilegeService privilegeService;

	public String list(){
		if(role!=null&&role.getId()!=null&&role.getId()==0){
			role=null;
		}
		rolelist=roleService.findByObject(role);
		if(rolelist==null){
			pager=roleService.findPage(pageNum, numPerPage, orderField, orderDirection);
			rolelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlook(){
		rolelist=roleService.findByObject(role);
		if(rolelist==null){
			pager=roleService.findPage(pageNum, numPerPage, orderField, orderDirection);
			rolelist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlooks(){
		rolelist=roleService.findByObject(role);
		if(rolelist==null){
			rolelist=roleService.getAll();
		}
		return SUCCESS;
	}

	public String insertUI(){
		return SUCCESS;
	}

	public void insert(){
		if (privileges != null && !privileges.getIds().equals("")) {
			role.setPrivileges(new HashSet(privilegeService.findByIds(privileges.getIds())));
		} else {
			role.setPrivileges(null);
		}
		if (users != null && !users.getIds().equals("")) {
			role.setUsers(new HashSet(userService.findByIds(users.getIds())));
		} else {
			role.setUsers(null);
		}
		roleService.save(role);
		super.returnjson("rolelist");
	}

	public String updateUI(){
		if(role.getId()!=null){
			role=roleService.find(role.getId());
			ids="";names="";
			for (Object obj: role.getPrivileges()) {  
				if(obj instanceof Privilege){ 
					Privilege oj=(Privilege) obj;
					ids+=oj.getId()+",";
					names+=oj.getPriname()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			privileges=new Privilege();
			privileges.setIds(ids);
			privileges.setNames(names);
			ids="";names="";
			for (Object obj: role.getUsers()) {  
				if(obj instanceof User){ 
					User oj=(User) obj;
					ids+=oj.getId()+",";
					names+=oj.getRealname()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			users=new User();
			users.setIds(ids);
			users.setNames(names);
		}
		return SUCCESS;
	}

	public void update(){
		if (privileges != null && !privileges.getIds().equals("")) {
			role.setPrivileges(new HashSet(privilegeService.findByIds(privileges.getIds())));
		} else {
			role.setPrivileges(null);
		}
		if (users != null && !users.getIds().equals("")) {
			role.setUsers(new HashSet(userService.findByIds(users.getIds())));
		} else {
			role.setUsers(null);
		}
		roleService.update(role);
		super.returnjson("rolelist");
	}

	public String view(){
		if(role.getId()!=null){
			role=roleService.find(role.getId());
			ids="";names="";
			for (Object obj: role.getPrivileges()) {  
				if(obj instanceof Privilege){ 
					Privilege oj=(Privilege) obj;
					ids+=oj.getId()+",";
					names+=oj.getPriname()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			privileges=new Privilege();
			privileges.setIds(ids);
			privileges.setNames(names);
			ids="";names="";
			for (Object obj: role.getUsers()) {  
				if(obj instanceof User){ 
					User oj=(User) obj;
					ids+=oj.getId()+",";
					names+=oj.getRealname()+",";
				}
			}
			if(ids.length()>0){ids=ids.substring(0, ids.length()-1);}
			if(names.length()>0){names=names.substring(0, names.length()-1);}
			users=new User();
			users.setIds(ids);
			users.setNames(names);
		}
		return SUCCESS;
	}

	public void del(){
		StringTokenizer stk=new StringTokenizer(roleids, ",");
		List<Integer> lds=new ArrayList<Integer>();
		while(stk.hasMoreElements()){
			lds.add(Integer.valueOf(stk.nextElement().toString()));
		}
		roleService.deleteByIds(lds.toArray());
		super.returnjsondel("rolelist");
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public List<Role> getRolelist() {
		return rolelist;
	}

	public void setRolelist(List<Role> rolelist) {
		this.rolelist = rolelist;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getRoleids() {
		return roleids;
	}

	public void setRoleids(String roleids) {
		this.roleids = roleids;
	}

	public Privilege getPrivileges() {
		return privileges;
	}

	public void setPrivileges(Privilege privileges) {
		this.privileges = privileges;
	}

	public User getUsers() {
		return users;
	}

	public void setUsers(User users) {
		this.users = users;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public PrivilegeService getPrivilegeService() {
		return privilegeService;
	}

	public void setPrivilegeService(PrivilegeService privilegeService) {
		this.privilegeService = privilegeService;
	}



}