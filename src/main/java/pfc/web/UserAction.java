package pfc.web;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.json.JSONObject;
import pfc.bean.Role;
import pfc.bean.User;
import pfc.service.role.RoleService;
import pfc.service.user.UserService;

public class UserAction extends BaseAction {

	private UserService userService;
	private List<User> userlist;
	private User user;
	private String userids;
	private Role roles;
	private RoleService roleService;

	public String list(){
		if(user!=null&&user.getId()!=null&&user.getId()==0){
			user=null;
		}
		userlist=userService.findByObject(user);
		if(userlist==null){
			pager=userService.findPage(pageNum, numPerPage, orderField, orderDirection);
			userlist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}

	public String listlook(){
		userlist=userService.findByObject(user);
		if(userlist==null){
			pager=userService.findPage(pageNum, numPerPage, orderField, orderDirection);
			userlist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}
	public String listlookForTeacher(){
		if(user==null)user=new User();
		user.setUsertype(0);
		userlist=userService.findByObject(user);
		if(userlist==null){
			pager=userService.findPage(pageNum, numPerPage, orderField, orderDirection);
			userlist=pager.getList();
			super.page();
		}
		return SUCCESS;
	}
	
	public String listlookForStudent(){
		user=new User();
		user.setUsertype(1);
		userlist=userService.findByObject(user);
		if(userlist==null){
			userlist=userService.getAll();
		}
		return SUCCESS;
	}
	public String listlooks(){
		userlist=userService.findByObject(user);
		if(userlist==null){
			userlist=userService.getAll();
		}
		return SUCCESS;
	}
	public String insertUI(){
		return SUCCESS;
	}

	public void insert(){
		if (roles != null && !roles.getIds().equals("")) {
			user.setRoles(new HashSet(roleService.findByIds(roles.getIds())));
		} else {
			user.setRoles(null);
		}
		userService.save(user);
		super.returnjson("userlist");
	}
	public void regist(){
		user.setRoles(new HashSet(roleService.findByIds("22")));
		userService.save(user);
		out("注册成功");
	}
	public String updateUI(){
		if(user.getId()!=null){
			user=userService.find(user.getId());
			ids="";names="";
			for (Object obj: user.getRoles()) {  
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
			user.setRoles(new HashSet(roleService.findByIds(roles.getIds())));
		} else {
			user.setRoles(null);
		}
		userService.update(user);
		super.returnjson("userlist");
	}

	public String view(){
		if(user.getId()!=null){
			user=userService.find(user.getId());
			ids="";names="";
			for (Object obj: user.getRoles()) {  
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
		StringTokenizer stk=new StringTokenizer(userids, ",");
		List<Integer> lds=new ArrayList<Integer>();
		while(stk.hasMoreElements()){
			lds.add(Integer.valueOf(stk.nextElement().toString()));
		}
		userService.deleteByIds(lds.toArray());
		super.returnjsondel("userlist");
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<User> getUserlist() {
		return userlist;
	}

	public void setUserlist(List<User> userlist) {
		this.userlist = userlist;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserids() {
		return userids;
	}

	public void setUserids(String userids) {
		this.userids = userids;
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