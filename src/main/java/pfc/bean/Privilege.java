package pfc.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * Privilege entity. @author MyEclipse Persistence Tools
 */

public class Privilege extends pfc.bean.base.Basebean implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String priurl;
	private String priname;
	private Integer type;
	private Set roles = new HashSet(0);

	// Constructors

	/** default constructor */
	public Privilege() {
	}

	/** full constructor */
	public Privilege(String priurl, String priname, Integer type, Set roles) {
		this.priurl = priurl;
		this.priname = priname;
		this.type = type;
		this.roles = roles;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPriurl() {
		return this.priurl;
	}

	public void setPriurl(String priurl) {
		this.priurl = priurl;
	}

	public String getPriname() {
		return this.priname;
	}

	public void setPriname(String priname) {
		this.priname = priname;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Set getRoles() {
		return this.roles;
	}

	public void setRoles(Set roles) {
		this.roles = roles;
	}

}