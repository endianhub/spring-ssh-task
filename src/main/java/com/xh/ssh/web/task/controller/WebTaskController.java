package com.xh.ssh.web.task.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xh.ssh.web.task.base.controller.BaseController;
import com.xh.ssh.web.task.common.annotation.AroundAspect;
import com.xh.ssh.web.task.common.exception.ResultException;
import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.common.tool.Assert;
import com.xh.ssh.web.task.common.tool.DateFormatTool;
import com.xh.ssh.web.task.common.tool.JedisClientTool;
import com.xh.ssh.web.task.common.tool.LogTool;
import com.xh.ssh.web.task.common.tool.TaskPoolTool;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IWebTaskService;
import com.xh.ssh.web.task.service.scheduler.ISchedulerManageService;


/**
 * <b>Title: 任务管理</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
@Controller
public class WebTaskController extends BaseController {

	@Autowired
	private IWebTaskService taskService;
	@Autowired
	private ISchedulerManageService schedulerManageService;

	@RequestMapping("index")
	public String index() {

		return "easyui.index";
	}

	@RequestMapping("query")
	@ResponseBody
	@AroundAspect
	public Object query() {
		List<WebTask> list = taskService.loadAll(WebTask.class);
		return list;
	}

	@PostMapping("save")
	@ResponseBody
	public Object save(WebTask task) {
		LogTool.info(this.getClass(), task);
		Assert.notNull(task, "对象不能为空！");
		boolean isRepeatQuest = false;
		String time = DateFormatTool.parseDateToString(new Date(), DateFormatTool.DATA_FORMATTER3_3);
		String key = this.getClass().getSimpleName() + "_" + task;
		try {
			if (JedisClientTool.isExist(key, time)) {
				isRepeatQuest = true;
				throw new ResultException("请求正在处理中，请勿重复提交");
			}
			task.setStatus(1);// 未部署
			task.setCreateTime(new Date());
			taskService.save(task);
			return super.renderSuccess();
		} finally {
			if (!isRepeatQuest) {
				JedisClientTool.del(key);
			}
		}
	}

	@PostMapping("edit")
	@ResponseBody
	public Object edit(WebTask task) {
		LogTool.info(this.getClass(), task);
		Assert.notNull(task, "对象不能为空！");
		Assert.isTrue((task.getStatus() != null && task.getStatus() == 1), "任务已部署！");
		boolean isRepeatQuest = false;
		String time = DateFormatTool.parseDateToString(new Date(), DateFormatTool.DATA_FORMATTER3_3);
		String key = this.getClass().getSimpleName() + "_" + task;
		try {
			if (JedisClientTool.isExist(key, time)) {
				isRepeatQuest = true;
				throw new ResultException("请求正在处理中，请勿重复提交");
			}

			Map<String, String> excludeFieldMap = new HashMap<String, String>();
			excludeFieldMap.put("createTime", "createTime");
			excludeFieldMap.put("status", "status");

			Object obj = taskService.update(task, excludeFieldMap, false);
			return super.renderSuccess();
		} finally {
			if (!isRepeatQuest) {
				JedisClientTool.del(key);
			}
		}
	}

	@RequestMapping("remove")
	@ResponseBody
	public Object remove(String taskId) {
		LogTool.info(this.getClass(), taskId);
		Assert.notNull(taskId, "任务ID不能为空！");

		Object obj = taskService.deleteById(this.paramSplit(taskId));

		Assert.isTrue((obj != null && !"".equals("")), obj + "任务已部署！");
		return super.renderSuccess();
	}

	/**
	 * <b>Title: 执行</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 */
	@RequestMapping("execute")
	@ResponseBody
	public Object execute(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");

		String name = "";
		List<Integer> list = this.paramSplit(taskId);
		for (Integer tid : list) {
			WebTask task = TaskPoolTool.get(tid + "");
			if (task == null) {
				task = (WebTask) taskService.load(WebTask.class, tid);
			}

			boolean flag = schedulerManageService.quartzExecTask(task);
			LogTool.debug(this.getClass(), task.getTaskId() + " - " + task.getTaskName() + "执行结果" + flag);
			if (!flag) {
				name += task.getTaskName() + "、";
			}
		}
		if ("".equals(name)) {
			return super.renderSuccess();
		} else {
			return super.renderError("任务：" + name + "执行失败");
		}

	}

	/**
	 * <b>Title: 部署</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param taskId
	 */
	@RequestMapping("deploy")
	@ResponseBody
	public Object deploy(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");
		String name = "";
		List<Integer> list = this.paramSplit(taskId);
		for (Integer tid : list) {
			WebTask task = (WebTask) taskService.load(WebTask.class, tid);
			Result result = schedulerManageService.quartzDeployTask(task);
			LogTool.debug(this.getClass(), result);

			if (result.getCode() != 200) {
				name += task.getTaskName() + "，";
			}
		}
		if ("".equals(name)) {
			return super.renderSuccess();
		} else {
			return super.renderError("任务：" + name + "部署失败");
		}
	}

	// 取消部署
	@RequestMapping("undeploy")
	@ResponseBody
	public Object undeploy(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");

		boolean flag = schedulerManageService.quartzUndeployTask(taskId);
		LogTool.debug(this.getClass(), flag);
		if (flag) {
			return super.renderSuccess();
		}

		return super.renderError("任务：" + taskId + " 部署失败");
	}

	// 暂停定时任务
	@RequestMapping("pause")
	@ResponseBody
	public Object pause(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");

		boolean flag = schedulerManageService.quartzPauseTask(taskId);
		LogTool.debug(this.getClass(), flag);
		if (flag) {
			return super.renderSuccess();
		}

		return super.renderError("任务：" + taskId + " 部署失败");
	}

	// 恢复
	@RequestMapping("restore")
	@ResponseBody
	public Object restoreTask(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");

		boolean flag = schedulerManageService.quartzRestoreTask(taskId);
		LogTool.debug(this.getClass(), flag);
		if (flag) {
			return super.renderSuccess();
		}

		return super.renderError("任务：" + taskId + " 部署失败");
	}

	// 关闭定时任务
	@RequestMapping("shutdown")
	@ResponseBody
	public Object shutdown(String taskId) {
		Assert.isBlank(taskId, "任务ID不能为空！");

		boolean flag = schedulerManageService.quartzShutdownTask(taskId);
		LogTool.debug(this.getClass(), flag);
		if (flag) {
			return super.renderSuccess();
		}

		return super.renderError("任务：" + taskId + " 部署失败");
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
