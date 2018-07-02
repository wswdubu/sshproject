package pfc.service.login.impl;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import pfc.bean.User;
import pfc.service.login.LoginService;
import pfc.service.user.UserService;
public class LoginServiceImpl implements LoginService {
	private UserService userService;
	private User user;
	public boolean loginIn(String username, String password , String Code)
			throws Exception {
		//subject理解成权限对象。类似user
		Subject subject = SecurityUtils.getSubject();
		Session session=subject.getSession();
		/*//验证验证码是否正确
		if(!session.getAttribute("ccode").equals(Code)){
			request.setAttribute("msg", "验证码错误");
			return false;
		}*/
		//创建用户名和密码的令牌
		UsernamePasswordToken token = new UsernamePasswordToken(username,password);
		//记录该令牌，如果不记录则类似购物车功能不能使用。
		token.setRememberMe(true);
		try {
		subject.login(token);
		} catch (UnknownAccountException ex) {	
			System.out.println("不存在");
		} catch (IncorrectCredentialsException ex) {
			System.out.println("密码错误");
		}catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println("服务器忙");
		}
		//验证是否成功登录的方法
		if (subject.isAuthenticated()) {
			List<User> ul=userService.queryByPropertye("userid", username);
			if(ul.size()>0){
				user=ul.get(0);
			}
			session.setAttribute("currentuser", user);
			return true;
		}
		return false;
	}

	public boolean loginOut() {
		Subject subject = SecurityUtils.getSubject();	
		subject.logout();//注销当前用户
		return true;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}




}
