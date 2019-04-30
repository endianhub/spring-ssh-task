package com.xh.ssh.web.task.common.tool;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.xh.ssh.web.task.model.WebTask;

/**
 * <b>Title: 任务池</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月5日
 */
public class TaskPoolTool {

	public static Map<String, Object> getTasksMap() {
		String json = (String) JedisClientTool.get(WebTask.class.getSimpleName());
		if (json == null) {
			return new HashMap<String, Object>();
		}
		Map<String, Object> map = JSON.parseObject(json);
		return map;
	}

	public static void remove(String taskId) {
		Map<String, Object> map = getTasksMap();
		Object obj = map.remove(taskId);
		LogTool.debug(TaskPoolTool.class, "task removed :" + obj);
		JedisClientTool.set(WebTask.class.getSimpleName(), JSON.toJSONString(map));
	}

	public static int size() {
		Map<String, Object> map = getTasksMap();
		for (Entry<String, Object> entry : map.entrySet()) {
			LogTool.debug(TaskPoolTool.class, entry.getKey() + "--->" + entry.getValue());
		}
		return map.size();
	}

	public static boolean containsKey(String taskId) {
		Map<String, Object> map = getTasksMap();
		return map.containsKey(taskId);
	}

	public static void put(String taskId, WebTask task) {
		Map<String, Object> map = getTasksMap();
		map.put(taskId, JSON.toJSON(task));
		JedisClientTool.set(task.getClass().getSimpleName(), JSON.toJSONString(map));
	}

	public static WebTask get(String taskId) {
		Map<String, Object> map = getTasksMap();
		if (map == null || map.size() == 0 || map.get(taskId) == null) {
			return null;
		}
		return JSON.parseObject(map.get(taskId).toString(), WebTask.class);
	}

}
