package com.xh.ssh.web.task.base.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.xh.ssh.web.task.common.exception.ExceptionCode;
import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.common.tool.StringEscapeEditorTool;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月24日
 */
public abstract class BaseController {

	@InitBinder
	public void initBinder(ServletRequestDataBinder binder) {
		/**
		 * 自动转换日期类型的字段格式
		 */
		binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));
		/**
		 * 防止XSS攻击
		 */
		binder.registerCustomEditor(String.class, new StringEscapeEditorTool());
	}

	/**
	
	/**
	 * ajax失败
	 * @param msg 失败的消息
	 * @return {Object}
	 */
	public Object renderError(String msg) {
		return Result.exception(msg);
	}

	/**
	 * ajax失败
	 * @param code 消息类型
	 * @param msg 失败的消息
	 * @return {Object}
	 */
	public Object renderError(Integer code, String msg) {
		return Result.exception(code, msg);
	}

	/**
	 * ajax成功
	 * @return {Object}
	 */
	public Object renderSuccess() {
		return Result.exception(ExceptionCode.SUCCESS.code, ExceptionCode.SUCCESS.msg);
	}

	/**
	 * ajax成功
	 * @param msg 消息
	 * @return {Object}
	 */
	public Object renderSuccess(String msg) {
		return Result.exception(ExceptionCode.SUCCESS.code, msg);
	}

	/**
	 * <p>Title: ajax成功</p>
	 * <p>Description: </p>
	 * 
	 * @param msg 消息
	 * @param obj 成功时的对象
	 * @return
	 */
	public Object renderSuccess(String msg, Object obj) {
		return Result.exception(ExceptionCode.SUCCESS.code, msg, obj);
	}

	/**
	 * <p>Title: ajax成功</p>
	 * <p>Description: </p>
	 * 
	 * @param code 消息类型
	 * @param msg 消息
	 * @param obj 成功时的对象
	 * @return
	 */
	public Object renderSuccess(Integer code, String msg, Object obj) {
		return Result.exception(code, msg, obj);
	}

	/**
	 * redirect跳转
	 * @param url 目标url
	 */
	protected String redirect(String url) {
		return new StringBuilder("redirect:").append(url).toString();
	}

}
