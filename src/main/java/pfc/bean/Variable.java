package pfc.bean;

/**
 * Variable entity. @author MyEclipse Persistence Tools
 */

public class Variable extends pfc.bean.base.Basebean implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private String varname;
	private String varvalue;
	private String returnname;

	// Constructors

	/** default constructor */
	public Variable() {
	}

	/** full constructor */
	public Variable(String varname, String varvalue, String returnname) {
		this.varname = varname;
		this.varvalue = varvalue;
		this.returnname = returnname;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVarname() {
		return this.varname;
	}

	public void setVarname(String varname) {
		this.varname = varname;
	}

	public String getVarvalue() {
		return this.varvalue;
	}

	public void setVarvalue(String varvalue) {
		this.varvalue = varvalue;
	}

	public String getReturnname() {
		return this.returnname;
	}

	public void setReturnname(String returnname) {
		this.returnname = returnname;
	}

}