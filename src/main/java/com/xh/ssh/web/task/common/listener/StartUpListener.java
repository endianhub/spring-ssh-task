package com.xh.ssh.web.task.common.listener;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.alibaba.fastjson.JSON;
import com.xh.ssh.web.task.common.tool.ConstantUtils;
import com.xh.ssh.web.task.common.tool.LogUtils;
import com.xh.ssh.web.task.common.tool.SpringUtils;
import com.xh.ssh.web.task.common.tool.TaskPoolUtils;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.scheduler.TriggerQuartzJobProxy;

/**
 * <b>Title: 自定义启动监听类</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2019年7月17日
 */
public class StartUpListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		SpringUtils.setServletContext(servletContext);

		this.schedulerManage();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	public void schedulerManage() {
		Map<String, Object> map = TaskPoolUtils.getTasksMap();
		try {
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				WebTask task = JSON.parseObject(entry.getValue().toString(), WebTask.class);

				// 执行次数到达计划执行次数，不再部署
				if (task.getPlanExec() > 0) {
					if (task.getExecuted().intValue() >= task.getPlanExec().intValue() || task.getStatus() == ConstantUtils.NUMBER_TWO) {
						LogUtils.info(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "执行次数到达计划执行次数！");
						break;
					}
				}

				Object target = SpringUtils.getSpringBean(task.getTaskClass());
				if (target == null) {
					LogUtils.info(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "，没有扫描到" + task.getTaskClass() + "类！");
					break;
				}

				Class<?> clazz = Class.forName(target.getClass().getName());
				// 把触发器需要调用的方法找出来，还是用反射
				Method method = clazz.getMethod("execute", WebTask.class);
				String taskId = String.valueOf(task.getTaskId());

				// 拿到Quartz中的调度器，方法一：
				Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

				// 创建一个 JobDetail
				JobDetail taskDetail = JobBuilder.newJob(TriggerQuartzJobProxy.class)// 任务名，任务组，任务执行类
						.withIdentity(JobKey.jobKey(taskId, task.getTaskClass()))//
						.build();

				// 触发器
				CronTrigger cronTrigger = TriggerBuilder.newTrigger()// 触发器名,触发器组
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
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// schedulerManage();
	}
}
