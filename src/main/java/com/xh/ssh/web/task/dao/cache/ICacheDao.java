package com.xh.ssh.web.task.dao.cache;

import java.util.Map;

/**
 * <b>Title: 缓存Redis</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 */
public interface ICacheDao {

	/**
	 * <b>Title: 刷新Redis缓存</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @return
	 */
	public Map<Object, Object> loadTableToCache();
}
