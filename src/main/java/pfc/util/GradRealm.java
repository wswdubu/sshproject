package pfc.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import pfc.bean.Privilege;
import pfc.bean.Role;
import pfc.bean.User;
import pfc.service.user.UserService;

public class GradRealm extends AuthorizingRealm {

	private UserService userService;
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName = (String) getAvailablePrincipal(principals);
		User user=null;
		List<User> userl=userService.queryByPropertye("userid", userName);
		if(userl.size()>0){
			user=userl.get(0);
		}
		//此处只是负责查询系统的权限存到到info中
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> priset=new HashSet<String>();
		Set<String> roless=new HashSet<String>();
		if(user!=null){
			Set<Role> roleset=user.getRoles();
			for(Role role:roleset){
				Set<Privilege> syspriset=role.getPrivileges();
				for(Privilege pri:syspriset){
					priset.add(pri.getPriname());
				}
				roless.add(role.getRolename());
			}
			info.setStringPermissions(priset);
			info.setRoles(roless);
		}
		this.clearCachedAuthorizationInfo(principals);
		
		/*info.setRoles(set集合);
		info.setObjectPermissions(set集合);*/
		
		return info;

	}
	
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		//token中储存着输入的用户名和密码
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		//获得用户名与密码
		String username = upToken.getUsername();
		String password = String.valueOf(upToken.getPassword());
		User user=null;
		List<User> userl=userService.queryByPropertye("userid", username);
		if(userl.size()>0){
			user=userl.get(0);
		}
		//用户不存在
		if(user==null)
		{
			throw new UnknownAccountException();
		}
		
		//用户名、密码正确
		if(user.getPassword().equals(password)){
		//if(user.getPassword().equals(SHA_256.SHA_256(password))){
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username, password .toCharArray(),getName());
			return info;
		}
		//密码错误
		else if(!user.getPassword().equals(SHA_256.SHA_256(password)))
		{
			throw new IncorrectCredentialsException();
		}
		return null;
	}

	public void clearCachedAuthorizationInfo(Object object){
		SimplePrincipalCollection prin = new SimplePrincipalCollection(object, getName());
		clearCachedAuthorizationInfo(prin);
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}



}
