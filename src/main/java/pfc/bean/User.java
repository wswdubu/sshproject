package pfc.bean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User extends pfc.bean.base.Basebean implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String userid;
	private String realname;
	private String password;
	private Integer gender;
	private Date birthdate;
	private String identifynum;
	private String email;
	private String phone;
	private String homeaddress;
	private Timestamp createtime;
	private String description;
	private Set roles = new HashSet(0);
	private Integer usertype;
	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(String userid, String realname, String password,
			Integer gender, Date birthdate, String identifynum, String email,
			String phone, String homeaddress, Timestamp createtime,
			String description, Set roles) {
		this.userid = userid;
		this.realname = realname;
		this.password = password;
		this.gender = gender;
		this.birthdate = birthdate;
		this.identifynum = identifynum;
		this.email = email;
		this.phone = phone;
		this.homeaddress = homeaddress;
		this.createtime = createtime;
		this.description = description;
		this.roles = roles;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGender() {
		return this.gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getIdentifynum() {
		return this.identifynum;
	}

	public void setIdentifynum(String identifynum) {
		this.identifynum = identifynum;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHomeaddress() {
		return this.homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public Timestamp getCreatetime() {
		return this.createtime;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUsertype() {
		return usertype;
	}

	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}

	public Set getRoles() {
		return this.roles;
	}

	public void setRoles(Set roles) {
		this.roles = roles;
	}


}