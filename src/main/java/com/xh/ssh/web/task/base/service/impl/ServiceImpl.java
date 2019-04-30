package com.xh.ssh.web.task.base.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.xh.ssh.web.task.base.dao.IHibernateDao;
import com.xh.ssh.web.task.base.service.IService;
import com.xh.ssh.web.task.common.tool.Assert;
import com.xh.ssh.web.task.common.tool.BeanTool;
import com.xh.ssh.web.task.common.tool.LogTool;

/**
 * <b>Title: IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年10月24日
 */
@SuppressWarnings("all")
public class ServiceImpl<M extends IHibernateDao<T, Serializable>, T> implements IService<T> {

	@Autowired
	protected M defaultDao;

	@Override
	public <T> Object save(T entity) {

		return defaultDao.saveObject(entity);
	}

	@Override
	public <T> Object update(T entity) {

		return defaultDao.updateObject(entity);
	}

	@Override
	public <T> Object delete(T entity) {

		return defaultDao.deleteObject(entity);
	}

	@Override
	public <T> List<T> loadAll(Class<T> paramClass) {

		return defaultDao.loadAll(paramClass);
	}

	@Override
	public <T> Object get(Class<T> paramClass, Serializable paramSerializableId) {

		return defaultDao.getObject(paramClass, paramSerializableId);
	}

	@Override
	public <T> Object load(Class<T> paramClass, Serializable paramSerializableId) {

		return defaultDao.loadObject(paramClass, paramSerializableId);
	}

	@Override
	public <T> T loadTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap) {

		return defaultDao.loadTableByCloumn(paramClass, paramMap);
	}

	@Override
	public <T> List<T> selectTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap) {

		return defaultDao.selectTableByCloumn(paramClass, paramMap);
	}

	@Override
	public <T> List<T> loadTableByCloumn(T entity) {
		List<T> list = (List<T>) defaultDao.selectTableByCloumn(entity.getClass(), BeanTool.parseFromBeanToMap(entity, false));
		LogTool.info(this.getClass(), "The result is a list....");
		return list;
	}

	@Override
	public <T> Object loadTableByCloumnz(T entity) {
		List<T> list = (List<T>) this.loadTableByCloumn(entity);
		if (list.size() == 1) {
			return list.get(0);
		}
		Assert.isBlank(null, "The result is a list....");
		return null;
	}

	@Override
	public Object update(Object obj, Map<String, String> excludeFieldMap, boolean flag) {

		return defaultDao.updateObjectByHql(obj, excludeFieldMap, flag);
	}

	// @Override
	// public <T> void loadTableToCache(String beanName, Map<Object, Object> paramMap) {
	// // 获取泛型类Class对象，不是泛型类则返回null
	// Class<T> paramClass = null;
	// Class<T> paramDaoClass = null;
	// Type genericSuperclass = this.getClass().getGenericSuperclass();
	// if (genericSuperclass instanceof ParameterizedType) {
	// Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
	// if (actualTypeArguments != null && actualTypeArguments.length > 0) {
	// paramDaoClass = (Class<T>) actualTypeArguments[0];
	// paramClass = (Class<T>) actualTypeArguments[1];
	// }
	// }
	//
	// if (paramClass != null) {
	//// List<T> list = this.selectTableByCloumn(paramClass, paramMap);
	//// for (T model : list) {
	//// LogTool.info(this.getClass(), JSON.toJSONString(model));
	//// }
	//
	// List<Object[]> list = paramDaoClass.getClass().selectBySQLArray(null);
	// }
	// }
}
