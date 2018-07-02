package pfc.bean;

/**
 * Demo entity. @author MyEclipse Persistence Tools
 */

public class Demo extends pfc.bean.base.Basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;

	// Constructors

	/** default constructor */
	public Demo() {
	}

	/** full constructor */
	public Demo(String name) {
		this.name = name;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}


}