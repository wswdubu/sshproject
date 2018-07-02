package pfc.service.base;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import pfc.util.GenericsUtils;
import pfc.util.Pager;

public class DaoSupport<T> extends HibernateDaoSupport implements DAO<T> {
	protected Class<T> entityClass = GenericsUtils.getSuperClassGenricType(this
			.getClass());

	public Object executeMySQL(final String sql) {
		return getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Connection CurConn = SessionFactoryUtils.getDataSource(
						getSessionFactory()).getConnection();
				PreparedStatement ps = CurConn.prepareStatement(sql);
				ps.execute();
				ps.close();CurConn.close();
				session.flush();
				return null;
			}
		});
	}

	public void save(T entity) {
		getHibernateTemplate().save(entityClass.getName(), entity);
	}

	public List<T> findByIds(String ids) {
		List<Criterion> clist = new ArrayList<Criterion>();
		clist.add(Property.forName("id").in(
				parseStringToInteger(ids.split(","))));// 根据id集合查询权限
		return getListByCriteria(clist, null);
	}

	public List<T> findByObject(T entity) {
		if(entity==null){
			return null;
		}
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> rlist = new ArrayList<String>();
		Class userCla = null;
		try {
			userCla = Class.forName(entityClass.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fs = userCla.getDeclaredFields();
		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			f.setAccessible(true); // 设置些属性是可以访问的
			if (!f.getName().equals("id")) {
				String type = f.getType().toString();// 得到此属性的类型
				if (type.endsWith("String")) {
					PropertyDescriptor pd;
					try {
						pd = new PropertyDescriptor(f.getName(), entityClass);
						Method getMethod = pd.getReadMethod();// 获得get方法
						Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
						if(o!=null&&!o.toString().equals("")){
							clist.add(Restrictions.ilike(f.getName(), "%"+o+"%"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(type.endsWith("Integer") || type.endsWith("int")){
					PropertyDescriptor pd;
					try {
						pd = new PropertyDescriptor(f.getName(), entityClass);
						Method getMethod = pd.getReadMethod();// 获得get方法
						Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
						if(o!=null&&!o.toString().equals("")){
							clist.add(Restrictions.eq(f.getName(), o));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else if ((type.endsWith("Timestamp")||type.endsWith("Date"))&&!(f.getName().contains("Start")||f.getName().contains("End"))) {
					PropertyDescriptor pd;
					try {
						pd = new PropertyDescriptor(f.getName()+"Start", entityClass);
						Method getMethods = pd.getReadMethod();// 获得get方法
						Object os = getMethods.invoke(entity);// 执行get方法返回一个Object
						pd = new PropertyDescriptor(f.getName()+"End", entityClass);
						Method getMethode = pd.getReadMethod();// 获得get方法
						Object oe = getMethode.invoke(entity);// 执行get方法返回一个Object
						pd = new PropertyDescriptor(f.getName(), entityClass);
						Method getMethod = pd.getReadMethod();// 获得get方法
						Object o = getMethod.invoke(entity);// 执行get方法返回一个Object
						if(os!=null){
							clist.add(Restrictions.ge(f.getName(), os));
						}
						if(oe!=null){
							clist.add(Restrictions.le(f.getName(), oe));
						}
						if(o!=null){
							clist.add(Restrictions.eq(f.getName(), o));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		if(clist.size()==0){
			return null;
		}
		return getListByCriteria(clist, rlist);
	}

	public Pager findPage(int offset, int length, String orderField,
			String orderDirection) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> rlist = new ArrayList<String>();
		return getPagerForPage(clist, (offset - 1) * length, length,
				orderField, orderDirection, rlist);
	}

	public List<T> getAll() {
		return (List<T>) getHibernateTemplate().find("from " + entityClass.getName());
	}

	public T find(Serializable entityId) {
		return getHibernateTemplate().get(entityClass, entityId);
	}

	public void update(T entity) {
		getHibernateTemplate().merge(entityClass.getName(), entity);
	}

	public int deleteByIds(final Object[] ids) {
		final String queryString = "delete " + entityClass.getName()
				+ " where id in (:ids)";
		try {
			int delcount =  this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session) {
							Query query = session.createQuery(queryString);
							query.setParameterList("ids", ids);
							return query.executeUpdate();
						}
					});
			return delcount;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}

	public List<T> queryByPropertyl(String property, Object condi) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> alist = new ArrayList<String>();
		clist.add(Restrictions.ilike(property, "%" + condi + "%"));
		return getListByCriteria(clist, alist);
	}

	public List<T> queryByPropertye(String property, Object condi) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> alist = new ArrayList<String>();
		clist.add(Restrictions.eq(property, condi));
		return getListByCriteria(clist, alist);
	}

	public List<T> queryByPropertyen(String property, Object condi) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> alist = new ArrayList<String>();
		clist.add(Restrictions.ne(property, condi));
		return getListByCriteria(clist, alist);
	}

	public List<T> queryByObjectPropertye(String property, Object condi,
			String obj) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> alist = new ArrayList<String>();
		clist.add(Restrictions.eq(property, condi));
		alist.add(obj);
		return getListByCriteria(clist, alist);
	}

	public List<T> queryByObjectPropertyen(String property, Object condi,
			String obj) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> alist = new ArrayList<String>();
		clist.add(Restrictions.ne(property, condi));
		alist.add(obj);
		return getListByCriteria(clist, alist);
	}

	/**
	 * 根据条件分页查询
	 * */
	public List getListForPage(final List<Criterion> criterions,
			final int offset, final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), aliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				if (orderField != null && !orderField.equals("")
						&& orderDirection != null && !orderDirection.equals("")) {
					if (orderDirection.equals("asc")) {
						criteria.addOrder(Order.asc(orderField));
					} else if (orderDirection.equals("desc")) {
						criteria.addOrder(Order.desc(orderField));
					}
				}
				criteria.setFirstResult(offset);
				criteria.setMaxResults(length);
				return criteria.list();
			}
		});
		return list;
	}

	public Pager getPager(String property, Object pervalue, int currentPage,
			int numPerPage, String mid) {
		List<Criterion> clist = new ArrayList<Criterion>();
		List<String> rlist = new ArrayList<String>();
		if (!property.equals("") && !pervalue.equals("")) {
			clist.add(Restrictions.eq(property, pervalue));
		}
		if (!mid.equals("")) {
			rlist.add(mid);
		}
		return getPagerForPage(clist, (currentPage - 1) * numPerPage,
				numPerPage, null, null, rlist);
	}

	/**
	 * 根据条件分页查询---改进(现在都改成此分页方法)
	 * */
	@SuppressWarnings("unchecked")
	public Pager getPagerForPage(final List<Criterion> criterions,
			final int first, final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist) {
		Pager pgr = (Pager) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), aliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				Integer rowCount = (Integer) criteria.setProjection(
						Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				criteria
						.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				if (orderField != null && !orderField.equals("")
						&& orderDirection != null && !orderDirection.equals("")) {
					if (orderDirection.equals("asc")) {
						criteria.addOrder(Order.asc(orderField));
					} else if (orderDirection.equals("desc")) {
						criteria.addOrder(Order.desc(orderField));
					}
				}

				criteria.setFirstResult(first);
				criteria.setMaxResults(length);
				return new Pager(criteria.list(), rowCount.intValue());
			}
		});
		return pgr;
	}

	/**
	 * 根据条件分页查询---改进(多表关联查询设置多个别名)
	 * */
	@SuppressWarnings("unchecked")
	public Pager getPagerAliasForPage(final List<Criterion> criterions,
			final int offset, final int length, final String orderField,
			final String orderDirection, final List<String> aliaslist,
			final List<String> bliaslist) {
		Pager pgr = (Pager) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), bliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				Long rowCount = (Long) criteria.setProjection(
						Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				criteria
						.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
				if (orderField != null && !orderField.equals("")
						&& orderDirection != null && !orderDirection.equals("")) {
					if (orderDirection.equals("asc")) {
						criteria.addOrder(Order.asc(orderField));
					} else if (orderDirection.equals("desc")) {
						criteria.addOrder(Order.desc(orderField));
					}
				}

				criteria.setFirstResult(offset);
				criteria.setMaxResults(length);
				return new Pager(criteria.list(), rowCount.intValue());
			}
		});
		return pgr;
	}

	/**
	 * 将字符串数组转换为long数组
	 * */
	public Long[] parseStringToLong(String str[]) {
		Long[] longs = new Long[str.length];
		for (int i = 0; i < str.length; i++) {
			if (isNum(str[i])) {
				longs[i] = Long.parseLong(str[i]);
			}
		}
		return longs;
	}

	public Integer[] parseStringToInteger(String str[]) {
		Integer[] ints = new Integer[str.length];
		for (int i = 0; i < str.length; i++) {
			if (isNum(str[i])) {
				ints[i] = Integer.parseInt(str[i]);
			}
		}
		return ints;
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 根据条件不分页查询--带排序
	 * */
	public List getListByCriteria(final List<Criterion> criterions,
			final List<String> aliaslist, final String orderField,
			final String orderDirection) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), aliaslist.get(j));
					}
				}
				if(criterions!=null){
					for (int i = 0; i < criterions.size(); i++) {
						criteria.add(criterions.get(i));
					}
				}
				if (orderField != null && !orderField.equals("")
						&& orderDirection != null && !orderDirection.equals("")) {
					if (orderDirection.equals("asc")) {
						criteria.addOrder(Order.asc(orderField));
					} else if (orderDirection.equals("desc")) {
						criteria.addOrder(Order.desc(orderField));
					}
				}
				return criteria.list();
			}
		});
		return list;
	}

	/**
	 * 根据条件不分页查询--不带排序
	 * */
	public List getListByCriteria(final List<Criterion> criterions,
			final List<String> aliaslist) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), aliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				return criteria.list();
			}
		});
		return list;
	}

	/**
	 * 根据条件不分页查询
	 * */
	public List getListByCriteria(final List<Criterion> criterions,
			final List<String> aliaslist, final List<String> bliaslist) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), bliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				return criteria.list();
			}
		});
		return list;
	}

	/**
	 * 获取查询总数
	 * */
	public int getPageTotalCount(final List<Criterion> criterions,
			final List<String> aliaslist) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				if (aliaslist != null) {
					for (int j = 0; j < aliaslist.size(); j++) {
						criteria
								.createAlias(aliaslist.get(j), aliaslist.get(j));
					}
				}
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				return criteria.list();
			}
		});
		return list.size();
	}

	/**
	 * 获取查询总数
	 * */
	public int getPageTotalCount(final List<Criterion> criterions) {
		List list = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				for (int i = 0; i < criterions.size(); i++) {
					criteria.add(criterions.get(i));
				}
				return criteria.list();
			}
		});
		return list.size();
	}
}
