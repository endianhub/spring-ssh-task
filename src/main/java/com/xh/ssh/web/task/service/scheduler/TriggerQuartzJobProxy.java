package com.xh.ssh.web.task.service.scheduler;

import java.lang.reflect.Method;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.xh.ssh.web.task.common.tool.LogUtils;
import com.xh.ssh.web.task.common.tool.SpringACAUtils;
import com.xh.ssh.web.task.common.tool.TaskPoolUtils;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.scheduler.impl.SchedulerManageServiceImpl;

/**
 * <b>Title: 创建要被定执行的任务类</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月5日
 */
public class TriggerQuartzJobProxy implements Job {

	public static final String TARGET = "target"; // 目标对象，实例
	public static final String TRIGGER = "trigger"; // 方法名
	public static final String PARAMS = "trigger_params"; // 方法的参数值

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int flag = 1;
		try {
			// 获取参数信息
			JobDataMap data = context.getTrigger().getJobDataMap();
			Object target = data.get(TARGET);
			Method method = (Method) data.get(TRIGGER);
			Object[] params = (Object[]) data.get(PARAMS);

			// 调用触发器，用反射调用我们自己定义的方法
			method.invoke(target, params);

		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			flag = 0;
		} finally {
			Object[] params = (Object[]) context.getTrigger().getJobDataMap().get(PARAMS);
			// 更新任务执行次数
			WebTask task = (WebTask) params[0];
			task.setExecuted(task.getExecuted() + 1);
			SchedulerManageServiceImpl schedulerManageServiceImpl = SpringACAUtils.getSpringByBean(SchedulerManageServiceImpl.class);

			// 执行次数到达计划执行次数
			if (task.getPlanExec() > 0) {
				if (task.getExecuted().intValue() >= task.getPlanExec().intValue()) {
					LogUtils.warn(this.getClass(), task.getTaskClass() + " beyond planned exec time!!!");
					task.setStatus(2);
					// 必须先
					schedulerManageServiceImpl.quartzShutdownTask(String.valueOf(task.getTaskId()));
					// 然后
					TaskPoolUtils.remove(String.valueOf(task.getTaskId()));
				}
			}
			schedulerManageServiceImpl.updateWebTask(task);
			// 保存执行情况
			// .............
		}

	}

}
