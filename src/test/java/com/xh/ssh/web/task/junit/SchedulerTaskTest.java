package com.xh.ssh.web.task.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.common.tool.LogUtils;
import com.xh.ssh.web.task.common.tool.TaskPoolUtils;
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

	// 部署作业
	// @Test
	public void deploy() throws InterruptedException {
		String taskId = "1";
		String name = "";
		List<Integer> list = this.paramSplit(taskId);
		for (Integer tid : list) {
			WebTask task = (WebTask) taskService.load(WebTask.class, tid);
			Result result = schedulerManageService.quartzDeployTask(task);
			LogUtils.debug(this.getClass(), result);

			if (result.getCode() != 200) {
				name += task.getTaskName() + "，";
			}
		}
		if ("".equals(name)) {
			System.out.println("部署成功");
		} else {
			System.out.println("任务：" + name + "部署失败");
		}

		Thread.sleep(30 * 60 * 1000);
	}

	// 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	public void quartzModifyTaskCron() {

	}

	// 取消部署
	@Test
	public void undeployTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzUndeployTask(taskId);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 立即执行
	// @Test
	public void executeTask() {
		String taskId = "1,2,3";
		String[] arr = taskId.split(",");
		for (int i = 0; i < arr.length; i++) {
			WebTask task = TaskPoolUtils.get(arr[i]);
			if (task == null) {
				task = (WebTask) taskService.load(WebTask.class, Integer.valueOf(arr[i]));
			}
			System.out.println(task);
			boolean flag = schedulerManageService.quartzExecTask(task);
			System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
		}
	}

	// 重启任务
	public void quartzRestartTask() {

	}

	// 暂停定时任务
	// @Test
	public void pauseTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzPauseTask(taskId);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 恢复任务
	public void restoreTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzRestoreTask(taskId);
		LogUtils.debug(this.getClass(), flag);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 关闭定时任务
	public void closeTask() {
		String taskId = "1";
		boolean flag = schedulerManageService.quartzShutdownTask(taskId);
		LogUtils.debug(this.getClass(), flag);
		System.out.println("\n\n\n=============" + flag + "==========\n\n\n");
	}

	// 启动所有定时任务
	public void quartzStartAllTask() {

	}

	// 关闭所有定时任务
	public void quartzShutdownAllTask() {

	}

	private List<Integer> paramSplit(String paramId) {
		String[] paramArrayOfObject = paramId.split(",");
		List<Integer> paramIds = new ArrayList<Integer>();
		for (String tid : paramArrayOfObject) {
			paramIds.add(Integer.valueOf(tid));
		}
		return paramIds;
	}
}
