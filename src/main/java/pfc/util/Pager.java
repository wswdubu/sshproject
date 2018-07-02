package pfc.util;

import java.util.List;

public class Pager {

	private List list;
	private Integer totalCount;
	public Pager() {
	}
	public Pager(List list, Integer totalCount) {
		super();
		this.list = list;
		this.totalCount = totalCount;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
	
	
}
