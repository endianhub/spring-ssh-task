package com.xh.ssh.web.task.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年10月24日
 */
@SuppressWarnings("all")
public interface IService<T> {

	public <T> Object save(T entity);

	public <T> Object update(T entity);

	public Object update(Object obj, Map<String, String> excludeFieldMap, boolean flag);

	public <T> Object delete(T entity);

	public <T> List<T> loadAll(Class<T> paramClass);

	public <T> Object get(Class<T> paramClass, Serializable paramSerializableId);

	public <T> Object load(Class<T> paramClass, Serializable paramSerializableId);

	public <T> T loadTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap);

	public <T> List<T> selectTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap);

	public <T> List<T> loadTableByCloumn(T entity);

	public <T> Object loadTableByCloumnz(T entity);

	// public <T> void loadTableToCache(String beanName, Map<Object, Object> paramMap);
}
