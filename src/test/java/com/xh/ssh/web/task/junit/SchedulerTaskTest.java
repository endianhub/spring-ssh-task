package com.xh.ssh.web.task.junit;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.ssh.web.task.common.tool.TaskPoolTool;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IWebTaskService;
import com.xh.ssh.web.task.service.scheduler.ISchedulerManageService;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2019年2月28日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-config.xml" })
public class SchedulerTaskTest {

	@Autowired
	private IWebTaskService taskService;

	@Autowired
	private ISchedulerManageService schedulerManageService;

	// 执行
//	@Test
	public void executeTask() {
		String taskId = "1,2,3";
		String[] arr = taskId.split(",");
		for (int i = 0; i < arr.length; i++) {
			WebTask task = TaskPoolTool.get(arr[i]);
			if (task == null) {
				task = (WebTask) taskService.load(WebTask.class, Integer.valueOf(arr[i]));
			}
			System.out.println(task);
			boolean flag = schedulerManageService.quartzExecTask(task);
			System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
		}
	}
	
	// 取消部署
	public void undeployTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzUndeployTask(taskId);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 暂停
	// @Test
	public void pauseTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzPauseTask(taskId);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 恢复
	public void restoreTask() {

	}

	// 关闭
	public void closeTask() {

	}
}
