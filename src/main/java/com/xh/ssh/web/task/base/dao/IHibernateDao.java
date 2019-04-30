package com.xh.ssh.web.task.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

/**
 * <b>Title: 基本DOA操作</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 */
@SuppressWarnings("all")
public interface IHibernateDao<T, PK extends Serializable> {

	/**
	 * <b>Title: getSession</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @return
	 */
	public Session getSession();

	public <T> Object deleteObject(T entity);

	/**
	 * <b>Title: HQL删除</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 */
	public <T> boolean delObjectByHql(String paramString);

	/**
	 * <b>Title: HQL删除</b>
	 * <p>Description: 绑定参数，参数按HQL顺序</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 * @param paramArrayOfObject HQL语句所绑定的参数
	 */
	public <T> boolean delObjectByHql(String paramString, Object[] paramArrayOfObject);

	/**
	 * <b>Title: SQL删除</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 */
	public <T> boolean delObjectBySql(String paramString);

	/**
	 * <b>Title: SQL删除</b>
	 * <p>Description: 绑定参数，参数按SQL顺序</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @param paramArrayOfObject SQL语句所绑定的参数
	 */
	public <T> boolean delObjectBySql(String paramString, Object[] paramArrayOfObject);

	/**
	 * <b>Title: 根据指定类和唯一索引查询</b>
	 * <p>Description: 只返回一条数据</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramClass 类
	 * @param paramSerializableId 唯一索引
	 * @return
	 */
	public <T> Object getObject(Class<T> paramClass, Serializable paramSerializableId);

	/**
	 * <b>Title: 根据指定实体类和唯一索引查询</b>
	 * <p>Description: 只返回一条数据</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param entityName 实体所在包路径全名
	 * @param paramSerializableId 唯一索引
	 * @return
	 */
	public <T> Object getObject(String entityName, Serializable paramSerializableId);

	/**
	 * <b>Title: 根据指定类查询所有</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramClass
	 * @return
	 */
	public <T> List<T> loadAll(Class<T> paramClass);

	/**
	 * <b>Title: 根据指定实体类和唯一索引查询</b>
	 * <p>Description: 只返回一条数据</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramClass 实体类
	 * @param paramSerializableId 唯一索引
	 * @return
	 */
	public <T> Object loadObject(Class<T> paramClass, Serializable paramSerializableId);

	/**
	 * <b>Title: 根据指定实体类查询指定条件的字段</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramClass 实体类
	 * @param paramMap 字段列-动态条件
	 * @return
	 */
	public <T> T loadTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap);

	/**
	 * <b>Title: 根据HQL语句查询列表</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 * @return
	 */
	public <T> List<T> loadTableByList(Serializable paramString);

	public <T> Object saveObject(T entity);

	/**
	 * <b>Title: HQL查询</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 * @return
	 */
	public <T> List<T> selectByHql(String paramString);

	/**
	 * <b>Title: HQL查询</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 * @param paramArrayOfObject HQL查询参数
	 * @return
	 */
	public <T> List<T> selectByHql(String paramString, Object[] paramArrayOfObject);

	/**
	 * <b>Title: SQL查询</b>
	 * <p>Description: 返回指定类型Map|Entity</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @param paramClass 返回类型 Map|Entity
	 * @return
	 */
	public <T> List<T> selectBySql(String paramString, Class<T> paramClass);

	/**
	 * <b>Title: SQL查询</b>
	 * <p>Description: 返回指定类型Map|Entity</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @param paramArrayOfObject SQL查询参数
	 * @param paramClass 返回类型 Map|Entity
	 * @return
	 */
	public <T> List<T> selectBySql(String paramString, Object[] paramArrayOfObject, Class<T> paramClass);

	/**
	 * <b>Title: SQL查询</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @return
	 */
	public <T> List<Object[]> selectBySQLArray(String paramString);

	/**
	 * <b>Title: SQL查询</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @param paramArrayOfObject SQL查询参数
	 * @return
	 */
	public <T> List<Object[]> selectBySQLArray(String paramString, Object[] paramArrayOfObject);

	/**
	 * <b>Title: 根据指定实体类查询指定条件的字段列表</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramClass 实体类
	 * @param paramMap 字段列-动态条件
	 * @return 
	 */
	public <T> List<T> selectTableByCloumn(Class<T> paramClass, Map<Object, Object> paramMap);

	public <T> Object updateObject(T entity);

	/**
	 * <b>Title: 修改数据</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 */
	public <T> boolean updateObjectByHql(String paramString);

	/**
	 * <b>Title: 修改数据</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString HQL语句
	 * @param paramArrayOfObject 要修改的参数
	 */
	public <T> boolean updateObjectByHql(String paramString, Object[] paramArrayOfObject);

	/**
	 * <b>Title: 修改数据</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param obj HQL语句
	 * @param excludeFieldMap 排除要更新的字段
	 * @param flag 判断是否显示空值，true 显示， false 不显示
	 */
	public <T> boolean updateObjectByHql(Object obj, Map<String, String> excludeFieldMap, boolean flag);

	/**
	 * <b>Title: 修改数据</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 */
	public <T> boolean updateObjectBySql(String paramString);

	/**
	 * <b>Title: 修改数据</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param paramString SQL语句
	 * @param paramArrayOfObject 要修改的参数
	 */
	public <T> boolean updateObjectBySql(String paramString, Object[] paramArrayOfObject);

}
