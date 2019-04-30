package com.xh.ssh.web.task.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.xh.ssh.web.task.base.dao.impl.HibernateDaoImpl;
import com.xh.ssh.web.task.dao.cache.ICacheDao;
import com.xh.ssh.web.task.model.WebTask;

/**
 * <b>Title: 任务操作</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
@Repository
@SuppressWarnings("all")
public class WebTaskDao extends HibernateDaoImpl<WebTask, Long> implements ICacheDao {

	@Override
	public Map<Object, Object> loadTableToCache() {
		Map<Object, Object> paramMap = new HashMap<Object, Object>();
		paramMap.put("status", 1);

		List<WebTask> list = (List<WebTask>) super.selectTableByCloumn(WebTask.class, paramMap);
		paramMap.clear();
		for (WebTask webTask : list) {
			paramMap.put(webTask.getClass().getSimpleName() + ":" + webTask.getTaskId(), JSON.toJSONString(webTask));
		}
		return paramMap;
	}

	public List<WebTask> selectByHql(List<Integer> paramIds) {
		String sql = "FROM WebTask WHERE taskId IN(:taskId) ";
		return getSession().createQuery(sql).setParameter("taskId", paramIds).list();
	}

	public void deleteById(List<Integer> paramIds) {
		String sql = "DELETE WebTask WHERE taskId IN (:taskId) AND status = 1 OR status=0 ";
		Query query = getSession().createQuery(sql).setParameter("taskId", paramIds);
		query.executeUpdate();
	}
}
