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
	 * <b>Title: 部署作业</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param task
	 * @return
	 */
	public Result quartzDeployTask(WebTask task);

	/**
	 * <b>Title: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名) </b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 * @param cron
	 * @return
	 */
	public boolean quartzModifyTaskCron(String taskId, String cron);

	/**
	 * <b>Title: 取消部署</b>
	 * <p>Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean quartzUndeployTask(String taskId);

	/**
	 * <b>Title: 立即执行</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param task
	 * @return
	 */
	public boolean quartzExecTask(WebTask task);

	/**
	 * <b>Title: 重启任务</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean quartzRestartTask(String taskId);

	/**
	 * <b>Title: 暂停定时任务</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
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
	 * <b>Title: 关闭定时任务</b>
	 * <p>Description: 只关闭当前当次任务，不做数据更新</p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean quartzShutdownTask(String taskId);

	/**
	 * <b>Title: 启动所有定时任务</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @return
	 */
	public boolean quartzStartAllTask();

	/**
	 * <b>Title: 关闭所有定时任务 </b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @return
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
