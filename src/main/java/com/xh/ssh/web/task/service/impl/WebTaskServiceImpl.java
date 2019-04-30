package com.xh.ssh.web.task.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xh.ssh.web.task.base.service.impl.ServiceImpl;
import com.xh.ssh.web.task.dao.WebTaskDao;
import com.xh.ssh.web.task.model.WebTask;
import com.xh.ssh.web.task.service.IWebTaskService;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月4日
 */
@Service
public class WebTaskServiceImpl extends ServiceImpl<WebTaskDao, WebTask> implements IWebTaskService {

	@Resource
	private WebTaskDao webTaskDao;

	@Override
	public Object deleteById(List<Integer> paramIds) {

		webTaskDao.deleteById(paramIds);

		String name = "";
		List<WebTask> list = webTaskDao.selectByHql(paramIds);
		for (WebTask webTask : list) {
			name += webTask.getTaskName() + "，";
		}
		return name;
	}

	@Override
	public Object execute(String[] taskId) {

		return null;
	}

}
