package com.xh.ssh.web.task.service;

import java.util.List;

import com.xh.ssh.web.task.base.service.IService;
import com.xh.ssh.web.task.model.WebTask;

/**
 * <b>Title: 任务管理</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
public interface IWebTaskService extends IService<WebTask> {

	public Object deleteById(List<Integer> paramIds);

	public Object execute(String[] taskId);
}
