package com.xh.ssh.web.task.junit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.xh.ssh.web.task.common.redis.JedisPoolUtils;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IWebTaskService;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class WebTaskServiceTest {

	@Resource
	private IWebTaskService taskService;

	// 清空缓存
	@Test
	public void flushAll() {
		JedisPoolUtils.flushAll();
	}

	// @Test
	public void testSan() {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
		Object target = context.getBean("doTask_MyTaskServiceImpl");
		System.out.println("\n\n target = " + target);
	}

	// @Test
	public void save() {
		WebTask task = new WebTask();
		task.setTaskName("测试");
		task.setTaskClass("DoTask_MyTaskServiceImpl");
		task.setTaskDesc("测试");
		// task.setTaskUrl(taskUrl);
		// task.setPlanExec(planExec);
		// task.setExecuted(executed);
		task.setCron("*/3 * * * * ?");
		task.setCronDesc("每隔3秒执行一次");
		// task.setNoticeList(noticeList);
		task.setStatus(1);
		task.setCreateTime(new Date());

		taskService.save(task);
	}

	// @Test
	public void query() {
		// List<WebTask> list = taskService.selectAll();
		List<WebTask> list = taskService.loadAll(WebTask.class);
		System.out.println(list.size());
	}

	// @Test
	public void delete() {
		String taskId = "1,8,9,";
		String[] paramArrayOfObject = taskId.split(",");
		List<Integer> paramIds = new ArrayList<Integer>();
		for (String tid : paramArrayOfObject) {
			paramIds.add(Integer.valueOf(tid));
		}

		taskService.deleteById(paramIds);
	}

	// @Test
	public void getAll() {
		WebTask task = new WebTask();
		task.setTaskId(1);

		// task = (WebTask) taskService.load(WebTask.class, 1);
		// task = (WebTask) taskService.get("com.xh.ssh.web.mapper.model.WebTask", 1);

		// Map<Object, Object> paramMap = new HashMap<Object, Object>();
		// paramMap.put("taskId", 1);
		// task = taskService.loadTableByCloumn(WebTask.class, paramMap);
		task = (WebTask) taskService.loadTableByCloumn(task);

		System.out.println(JSON.toJSONString(task));
	}

	// @Test
	public void get() {
		String taskId = "1";
		WebTask task = (WebTask) taskService.load(WebTask.class, Integer.valueOf(taskId));
		System.out.println(JSON.toJSONString(task));

		// taskService.update(task);
		// Map<String, String> excludeFieldMap = new HashMap<String, String>();
		// excludeFieldMap.put("createTime", "createTime");
		// excludeFieldMap.put("status", "status");
		// excludeFieldMap.put("noticeList", "noticeList");

		// taskService.update(task, null, true);

		// taskService.update(obj, excludeFieldMap, flag)
	}

	// @Test
	public void getArr() {

		// List<Object[]> arrs = taskService.selectArray();
		//
		// System.out.println(JSON.toJSONString(arrs));

	}
}
