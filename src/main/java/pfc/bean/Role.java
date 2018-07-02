package pfc.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Role entity. @author MyEclipse Persistence Tools
 */

public class Role extends pfc.bean.base.Basebean implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String rolename;
	private String roledesc;
	private Set privileges = new HashSet(0);
	private Set users = new HashSet(0);

	// Constructors

	/** default constructor */
	public Role() {
	}

	/** full constructor */
	public Role(String rolename, String roledesc, Set privileges, Set users) {
		this.rolename = rolename;
		this.roledesc = roledesc;
		this.privileges = privileges;
		this.users = users;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return this.roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public Set getPrivileges() {
		return this.privileges;
	}

	public void setPrivileges(Set privileges) {
		this.privileges = privileges;
	}

	public Set getUsers() {
		return this.users;
	}

	public void setUsers(Set users) {
		this.users = users;
	}

}