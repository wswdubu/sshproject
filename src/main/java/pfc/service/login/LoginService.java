package pfc.service.login;

public interface  LoginService {
	public abstract boolean loginIn(String name,String password,String Code)throws Exception;
	public abstract boolean loginOut();
}
