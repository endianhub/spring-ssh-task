package com.xh.ssh.web.task.base.dao.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.xh.ssh.web.task.base.dao.IHibernateDao;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年12月20日
 */
@SuppressWarnings("all")
public class HibernateDaoImpl<T, PK extends Serializable> extends HibernateDaoSupport implements IHibernateDao<T, Serializable> {

	@Resource(name = "sessionFactory")
	private void setMySessionFactory(SessionFactory sessionFactory) {
		// 这个方法名可以随便写，@Resource可以通过name 或者type来装载的。
		super.setSessionFactory(sessionFactory);
	}

	@Override
	public Session getSession() {

		return getSessionFactory().getCurrentSession();
	}

	@Override
	public <T> Object deleteObject(T entity) {
		Assert.notNull(entity, "实体类不能为空");
		getHibernateTemplate().delete(entity);
		return entity;
	}

	@Override
	public <T> boolean delObjectByHql(String paramString) {

		return this.modifyByHql(paramString, null);
	}

	@Override
	public <T> boolean delObjectByHql(String paramString, Object[] paramArrayOfObject) {

		return this.modifyByHql(paramString, paramArrayOfObject);
	}

	@Override
	public <T> boolean delObjectBySql(String paramString) {

		return this.modifyBySql(paramString, null);
	}

	@Override
	public <T> boolean delObjectBySql(String paramString, Object[] paramArrayOfObject) {

		return this.modifyBySql(paramString, paramArrayOfObject);
	}

	@Override
	public <T> Object getObject(Class<T> paramClass, Serializable paramSerializableId) {

		return getHibernateTemplate().get(paramClass, paramSerializableId);
	}

	@Override
	public <T> Object getObject(String entityName, Serializable paramSerializableId) {

		return getHibernateTemplate().get(entityName, paramSerializableId);
	}

	@Override
	public <T> List<T> loadAll(Class<T> paramClass) {

		return getHibernateTemplate().loadAll(paramClass);
	}

	@Override
	public <T> Object loadObject(Class<T> paramClass, Serializable paramSerializableId) {

		return getHibernateTemplate().get(paramClass, paramSerializableId);
	}

	@Override
	public <T> T loadTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap) {
		StringBuffer hql = new StringBuffer("FROM " + paramClass.getName() + " t where 1=1 ");
		return (T) getSession().createQuery(this.buildHQL(paramMap, hql).toString()).uniqueResult();
	}

	@Override
	public <T> List<T> loadTableByList(Serializable paramString) {

		return getSession().createQuery(paramString.toString()).list();
	}

	@Override
	public <T> Object saveObject(T entity) {
		Assert.notNull(entity, "实体类不能为空");
		return getHibernateTemplate().save(entity);
	}

	@Override
	public <T> List<T> selectByHql(String paramString) {

		return this.selectByHql(paramString, null);
	}

	@Override
	public <T> List<T> selectByHql(String paramString, Object[] paramArrayOfObject) {
		if ((null == paramArrayOfObject) || (paramArrayOfObject.length == 0)) {
			return (List<T>) getHibernateTemplate().find(paramString);
		}
		return (List<T>) getHibernateTemplate().find(paramString, paramArrayOfObject);
	}

	@Override
	public <T> List<T> selectBySql(String paramString, Class<T> paramClass) {

		return this.selectBySql(paramString, null, paramClass);
	}

	@Override
	public <T> List<T> selectBySql(String paramString, Object[] paramArrayOfObject, Class<T> paramClass) {
		NativeQuery<T> query = null;
		if (paramClass.getName().equals(Map.class.getName())) {
			query = getSession().createNativeQuery(paramString);
			query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		} else {
			if (paramClass != null) {
				query = getSession().createNativeQuery(paramString, paramClass);
			} else {
				query = getSession().createNativeQuery(paramString);
			}
		}

		if (null != paramArrayOfObject) {
			for (int i = 0; i < paramArrayOfObject.length; i++) {
				query.setParameter(i + 1, paramArrayOfObject[i]);
			}
		}
		return query.list();
	}

	@Override
	public <T> List<Object[]> selectBySQLArray(String paramString) {

		return this.selectBySQLArray(paramString, null);
	}

	@Override
	public <T> List<Object[]> selectBySQLArray(String paramString, Object[] paramArrayOfObject) {
		NativeQuery query = getSession().createNativeQuery(paramString);
		if (null != paramArrayOfObject) {
			for (int i = 0; i < paramArrayOfObject.length; i++) {
				query.setParameter(i + 1, paramArrayOfObject[i]);
			}
		}
		return query.list();
	}

	@Override
	public <T> List<T> selectTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap) {
		StringBuffer hql = new StringBuffer("FROM " + paramClass.getName() + " t where 1=1 ");
		return (List<T>) getSession().createQuery(this.buildHQL(paramMap, hql).toString()).list();
	}

	@Override
	public <T> Object updateObject(T entity) {
		Assert.notNull(entity, "实体类不能为空");
		getHibernateTemplate().update(entity);
		return entity;
	}

	@Override
	public <T> boolean updateObjectByHql(String paramString) {

		return this.modifyByHql(paramString, null);
	}

	@Override
	public <T> boolean updateObjectByHql(String paramString, Object[] paramArrayOfObject) {

		return this.modifyByHql(paramString, paramArrayOfObject);
	}

	@Override
	public <T> boolean updateObjectByHql(Object obj, Map<String, String> excludeFieldMap, boolean flag) {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE ");
		sb.append(obj.getClass().getSimpleName());
		sb.append(" SET ");

		String primaryKeyName = this.getPrimaryKeyname(obj);
		Assert.notNull(primaryKeyName, "未找到实体类主键标识!");
		Object primaryKeyValue = null;
		Map<Object, Object> paramMap = this.parseFromBeanToMap(obj, flag);
		for (Map.Entry<Object, Object> map : paramMap.entrySet()) {
			if (map.getKey().equals(primaryKeyName)) {
				primaryKeyValue = map.getValue();
				continue;
			}
			if ((excludeFieldMap != null && !excludeFieldMap.isEmpty()) && excludeFieldMap.get(map.getKey()) != null) {
				continue;
			}
			sb.append(map.getKey() + " = " + ((map.getValue() != null) ? "'" + map.getValue().toString() + "'" : null) + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append(" WHERE " + primaryKeyName + " = '" + primaryKeyValue + "'");
		System.out.println(sb.toString());
		return this.updateObjectByHql(sb.toString());
	}

	@Override
	public <T> boolean updateObjectBySql(String paramString) {

		return this.modifyBySql(paramString, null);
	}

	@Override
	public <T> boolean updateObjectBySql(String paramString, Object[] paramArrayOfObject) {

		return this.modifyBySql(paramString, paramArrayOfObject);
	}

	/********************************************************************************
	 * 
	 * 
	 * 私有方法
	 * 
	 * 
	 ********************************************************************************/

	private <T> boolean modifyBySql(String paramString, Object[] paramArrayOfObject) {
		NativeQuery<T> query = getSession().createNativeQuery(paramString);
		if (null != paramArrayOfObject) {
			for (int i = 0; i < paramArrayOfObject.length; i++) {
				System.out.println(paramArrayOfObject[i]);
				query.setParameter(i + 1, paramArrayOfObject[i]);
			}
		}
		query.executeUpdate();
		return true;
	}

	private <T> boolean modifyByHql(String paramString, Object[] paramArrayOfObject) {
		Query query = getSession().createQuery(paramString);
		if (null != paramArrayOfObject) {
			for (int i = 0; i < paramArrayOfObject.length; i++) {
				System.out.println(paramArrayOfObject[i]);
				query.setParameter(i + 1, paramArrayOfObject[i]);
			}
		}
		query.executeUpdate();
		return true;
	}

	private StringBuffer buildHQL(Map<Object, Object> map, StringBuffer hql) {
		Set es = map.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object val = entry.getValue();
			if (val instanceof String) {
				hql.append(" AND t." + key + " = '" + val + "' ");
			} else {
				hql.append(" AND t." + key + " = " + val + " ");
			}
		}
		return hql;
	}

	/**
	 * <b>Title: 将Bean对象转换成Map对象</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param clazz
	 * @param flag 判断是否显示空值，true 显示， false 不显示
	 * @return
	 */
	public static Map<Object, Object> parseFromBeanToMap(Object obj, boolean flag) {
		if (obj == null) {
			return null;
		}
		Map<Object, Object> map = new HashMap<Object, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				if (property.getName().equals("class")) {
					continue;
				}
				// 得到property对应的getter方法
				Method getMethod = property.getReadMethod();
				if (!flag) {
					if (getMethod.invoke(obj) == null || getMethod.invoke(obj) == "") {
						continue;
					}
					map.put(property.getName(), getMethod.invoke(obj));
				} else {
					map.put(property.getName(), getMethod.invoke(obj));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * <b>Title: 获取实体类主键名称  </b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param obj
	 * @return
	 */
	public String getPrimaryKeyname(Object obj) {
		String primaryKey = null;
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.isAnnotationPresent(Id.class);
			primaryKey = field.getName();
			break;
		}
		return primaryKey;
	}
}
