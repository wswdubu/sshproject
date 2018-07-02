package pfc.web;

import pfc.bean.User;
import pfc.service.login.LoginService;
import pfc.util.GradRealm;
import pfc.util.SpringContextHolder;

public class LoginAction extends BaseAction {
	
	private LoginService loginService;
	private String username;
	private String password;
	private User user;
	public String logout(){
		boolean result = false;
		user=currentuser();//获取当前登录用户
		result=loginService.loginOut();//用户注销登录
		if(result){
			return LOGIN;
		}
		return "main";
	}
	public String login(){
	boolean result = false;
		try {
			result=loginService.loginIn(username, password,null);//用户登录
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(result){
			/**登录清空缓存的权限信息*/
			SpringContextHolder.getBean(GradRealm.class).clearCachedAuthorizationInfo(username);
			return "main";
		}
			return LOGIN;
	}


	
	public LoginService getLoginService() {
		return loginService;
	}
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public User getUser() {
		return user;
	}
	

}
