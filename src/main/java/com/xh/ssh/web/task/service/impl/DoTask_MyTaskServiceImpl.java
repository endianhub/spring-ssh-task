package com.xh.ssh.web.task.service.impl;

import org.springframework.stereotype.Service;

import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IDoTaskService;

/**
 * <b>Title: 创建一个测试任务</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月12日
 */
@Service
public class DoTask_MyTaskServiceImpl implements IDoTaskService {

	@Override
	public void execute(WebTask task) {
		System.out.println(System.currentTimeMillis() + "AAAAAAAAAAAA");
	}

}
