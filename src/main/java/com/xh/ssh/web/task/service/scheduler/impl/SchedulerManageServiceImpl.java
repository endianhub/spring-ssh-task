package com.xh.ssh.web.task.service.scheduler.impl;

import java.lang.reflect.Method;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.common.tool.ConstantUtils;
import com.xh.ssh.web.task.common.tool.LogUtils;
import com.xh.ssh.web.task.common.tool.SpringACAUtils;
import com.xh.ssh.web.task.common.tool.TaskPoolUtils;
import com.xh.ssh.web.task.dao.WebTaskDao;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IDoTaskService;
import com.xh.ssh.web.task.service.scheduler.ISchedulerManageService;
import com.xh.ssh.web.task.service.scheduler.TriggerQuartzJobProxy;

/**
 * <b>Title: 定时器管理</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
@Service
public class SchedulerManageServiceImpl implements ISchedulerManageService {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private WebTaskDao webTaskDao;

	@Override
	public Result quartzDeployTask(WebTask task) {
		try {
			// 执行次数到达计划执行次数，不再部署
			if (task.getPlanExec() > 0) {
				if (task.getExecuted().intValue() >= task.getPlanExec().intValue() || task.getStatus() == ConstantUtils.NUMBER_TWO) {
					LogUtils.info(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "执行次数到达计划执行次数！");
					return Result.exception(ConstantUtils.FORBIDDEN, task.getTaskName() + "执行次数到达计划执行次数！");
				}
			}

			Object target = SpringACAUtils.getSpringBean(task.getTaskClass());
			// ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-config.xml");
			// Object target = context.getBean(task.getTaskClass());

			if (target == null) {
				LogUtils.info(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "，没有扫描到" + task.getTaskClass() + "类！");
				return Result.exception(ConstantUtils.NOT_FOUND, task.getTaskName() + "没有扫描到" + task.getTaskClass() + "类！");
			}

			Class<?> clazz = Class.forName(target.getClass().getName());
			// 把触发器需要调用的方法找出来，还是用反射
			Method method = clazz.getMethod("execute", WebTask.class);
			String taskId = String.valueOf(task.getTaskId());

			// 拿到Quartz中的调度器
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			// scheduler.isStarted()

			// 创建一个 JobDetail
			JobDetail taskDetail = JobBuilder.newJob(TriggerQuartzJobProxy.class)// 任务名，任务组，任务执行类
					.withIdentity(JobKey.jobKey(taskId, task.getTaskClass()))//
					.build();

			// 触发器
			CronTrigger cronTrigger = TriggerBuilder.newTrigger()// 触发器名,触发器组
					// .withIdentity(TriggerKey.triggerKey(taskId, "trigger"))//
					.withIdentity(TriggerKey.triggerKey(taskId, task.getTaskClass()))//
					.withSchedule(CronScheduleBuilder.cronSchedule(task.getCron()))//
					.build();

			// JobDataMap 用来存储附加信息
			// 利用这么一个API，把自定义的信息添加到Map中
			cronTrigger.getJobDataMap().put(TriggerQuartzJobProxy.TARGET, target);
			cronTrigger.getJobDataMap().put(TriggerQuartzJobProxy.TRIGGER, method);
			cronTrigger.getJobDataMap().put(TriggerQuartzJobProxy.PARAMS, new Object[] { task });

			scheduler.scheduleJob(taskDetail, cronTrigger);

			// 如果这个任务没有被主动关闭，就启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

			task.setStatus(2);// 已部署
			// this.updateWebTask(task);

			// 放入任务池
			boolean result = TaskPoolUtils.containsKey(String.valueOf(task.getTaskId()));
			if (!result) {
				TaskPoolUtils.put(String.valueOf(task.getTaskId()), task);
			}

			asynchUpdateWebTask(task);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | SchedulerException e) {
			LogUtils.info(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "部署失败", e);
			return Result.exception(ConstantUtils.INTERNAL_SERVER_ERROR, task.getTaskId() + "，部署失败");
		}
		return Result.exception(ConstantUtils.OK, "部署成功");
	}

	public void asynchUpdateWebTask(WebTask task) {
		new Thread() {
			public void run() {
				LogUtils.info(this.getClass(), " update WebTask ");
				webTaskDao.updateObject(task);
			}
		}.start();
	}

	@Override
	public boolean quartzModifyTaskCron(String taskId, String cron) {
		WebTask task = TaskPoolUtils.get(taskId);
		try {
			Scheduler sched = schedulerFactoryBean.getScheduler();
			CronTrigger trigger = (CronTrigger) sched.getTrigger(TriggerKey.triggerKey(taskId, task.getTaskClass()));
			String oldTime = trigger.getCronExpression();
			if (!oldTime.equalsIgnoreCase(cron)) {
				this.quartzUndeployTask(taskId);
				task.setCron(cron);
				this.quartzDeployTask(task);
			}
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzUndeployTask(String taskId) {
		try {
			WebTask task = TaskPoolUtils.get(taskId);
			if (task == null) {
				return false;
			}
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			// Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseTrigger(TriggerKey.triggerKey(taskId, task.getTaskClass()));// 停止触发器
			scheduler.unscheduleJob(TriggerKey.triggerKey(taskId, task.getTaskClass()));// 移除触发器
			scheduler.deleteJob(JobKey.jobKey(taskId, task.getTaskClass()));// 删除任务
			TaskPoolUtils.remove(taskId);

			task.setStatus(1);// 未部署
			this.updateWebTask(task);
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzExecTask(WebTask task) {
		try {
			LogUtils.debug(this.getClass(), "execute: " + task.toString());
			IDoTaskService doTaskService = SpringACAUtils.getSpringBean(task.getTaskClass());
			doTaskService.execute(task);
		} catch (Exception e) {
			LogUtils.error(this.getClass(), task, e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzRestartTask(String taskId) {
		try {
			WebTask task = TaskPoolUtils.get(taskId);
			Scheduler sched = schedulerFactoryBean.getScheduler();
			// 重启触发器
			sched.pauseTrigger(TriggerKey.triggerKey(taskId, task.getTaskClass()));
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzPauseTask(String taskId) {
		try {
			WebTask task = TaskPoolUtils.get(taskId);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			TriggerKey key = TriggerKey.triggerKey(taskId, task.getTaskClass());
			// 停止触发器
			scheduler.pauseTrigger(key);

			task.setStatus(3);// 已暂停
			this.updateWebTask(task);
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzRestoreTask(String taskId) {
		try {
			WebTask task = TaskPoolUtils.get(taskId);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			// 恢复触发器
			scheduler.resumeTrigger(TriggerKey.triggerKey(taskId, task.getTaskClass()));

			task.setStatus(2);// 已暂停
			this.updateWebTask(task);
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzShutdownTask(String taskId) {
		try {
			WebTask task = TaskPoolUtils.get(taskId);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			scheduler.pauseTrigger(TriggerKey.triggerKey(taskId, task.getTaskClass()));// 停止触发器
			scheduler.unscheduleJob(TriggerKey.triggerKey(taskId, task.getTaskClass()));// 移除触发器
			scheduler.deleteJob(JobKey.jobKey(taskId, task.getTaskClass()));// 删除任务

			task.setStatus(1);// 未部署
			this.updateWebTask(task);
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzStartAllTask() {
		try {
			Scheduler sched = schedulerFactoryBean.getScheduler();
			sched.start();
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public boolean quartzShutdownAllTask() {
		try {
			Scheduler sched = schedulerFactoryBean.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			LogUtils.error(this.getClass(), e);
			return false;
		}
		return true;
	}

	@Override
	public void updateWebTask(WebTask entity) {
		LogUtils.info(this.getClass(), " update WebTask ");
		webTaskDao.updateObject(entity);
	}

}
