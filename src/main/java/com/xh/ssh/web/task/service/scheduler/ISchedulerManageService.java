package com.xh.ssh.web.task.service.scheduler;

import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.model.WebTask;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
public interface ISchedulerManageService {

	/**
	 * 部署作业
	 * @param task
	 * @return
	 */
	public Result quartzDeployTask(WebTask task);

	/** 
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名) 
	 *  
	 */
	public boolean quartzModifyTaskCron(String taskId, String cron);

	/** 
	 * 取消部署<br>
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
	 *  
	 */
	public boolean quartzUndeployTask(String taskId);

	/** 
	 * 立即执行
	 *  
	 */
	public boolean quartzExecTask(WebTask task);

	/**
	 * 重启任务
	 * @param taskId
	 * @return
	 */
	public boolean quartzRestartTask(String taskId);

	/**
	 * 暂停定时任务
	 * @param taskId
	 * @return
	 */
	public boolean quartzPauseTask(String taskId);

	/**
	 * <b>Title: 恢复任务</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean quartzRestoreTask(String taskId);

	/**
	 * 关闭定时任务
	 * @param taskId
	 * @return
	 */
	public boolean quartzShutdownTask(String taskId);

	/** 
	 * 启动所有定时任务 
	 *  
	 */
	public boolean quartzStartAllTask();

	/** 
	 * 关闭所有定时任务 
	 */
	public boolean quartzShutdownAllTask();

	/**
	 * <b>Title: 更新任务</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param entity
	 */
	public void updateWebTask(WebTask entity);
}
