package com.xh.ssh.web.task.common.exception;

import com.xh.ssh.web.task.common.result.Result;

/**
 * <p>Title: 自定义异常</p>
 * <p>Description: 此类起到一个桥梁作用，遇到异常时传不同的code和message进去，然后封装成一个对象，然后再返回</p>
 * 
 * @author H.Yang
 * @date 2018年4月1日
 * 
 */
public class ResultException extends RuntimeException {

	private String msg;
	private int code = 500;

	public ResultException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public ResultException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public ResultException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public ResultException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public Result getException(Integer code, String message) {
		return Result.exception(code, message);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
