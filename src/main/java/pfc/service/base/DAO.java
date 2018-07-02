package pfc.service.base;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.Criterion;

import pfc.util.Pager;



public interface DAO<T> {
	public Object executeMySQL(final String sql);


	public List<T> getAll();
	
	
	public int deleteByIds(final Object[] ids);
	
	public T find(Serializable entityId);
	
	public void update(T entity);
	
	public List<T> queryByPropertye(String property,Object condi);
	
	public List<T> queryByPropertyen(String property,Object condi);
	
	public List<T> queryByPropertyl(String property,Object condi);
	
	public List<T> queryByObjectPropertye(String property,Object condi,String obj);
	
	public List<T> queryByObjectPropertyen(String property,Object condi,String obj);
	/**
	 * 根据条件分页查询
	 * */
	public List getListForPage(
			final List<Criterion> criterions, final int offset,
			final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist);
	
	/**
	 * 分页查询
	 * */
	public Pager getPager(String property,Object pervalue,int currentPage,int numPerPage,String mid);
	
	public Pager findPage(final int offset, final int length,String orderField,String orderDirection);
	public List<T> findByObject(T entity);
	/**
	 * 根据条件分页查询---改进(现在都改成此分页方法)
	 * */
	public Pager getPagerForPage(
			final List<Criterion> criterions, final int offset,
			final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist);
	/**
	 * 根据条件分页查询---改进(多表关联查询设置多个别名)
	 * */
	public Pager getPagerAliasForPage(
			final List<Criterion> criterions, final int offset,
			final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist,final List<String> bliaslist);
	/**
	 * 根据条件不分页查询--带排序
	 * */
	public List getListByCriteria(
			final List<Criterion> criterions, final List<String> aliaslist,final String orderField,
			final String orderDirection);
	public List<T> findByIds(String ids);
	/**
	 * 根据条件不分页查询--不带排序
	 * */
	public List getListByCriteria(
			final List<Criterion> criterions, final List<String> aliaslist);
	/**
	 * 根据条件不分页查询
	 * */
	public List getListByCriteria(
			final List<Criterion> criterions, final List<String> aliaslist,final List<String> bliaslist);
	/**
	 * 获取查询总数
	 * */
	public int getPageTotalCount(
			final List<Criterion> criterions, final List<String> aliaslist) ;
	/**
	 * 获取查询总数
	 * */
	public int getPageTotalCount(
			final List<Criterion> criterions);
	public void save(T entity);
}
