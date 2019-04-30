package com.xh.ssh.web.task.common.result;

/**
 * <p>Title: 操作结果集,返回数据</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 *
 */
public class Result<T> {

	private Integer code;
	private String msg;
	private T obj;

	public Result() {
		super();
	}

	public Result(String msg) {
		super();
		this.msg = msg;
	}

	public Result(Integer code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public Result(Integer code, String msg, T obj) {
		super();
		this.code = code;
		this.msg = msg;
		this.obj = obj;
	}

	public static Result exception(String msg) {
		return new Result(msg);
	}

	public static Result exception(Integer code, String msg) {
		return new Result(code, msg);
	}

	public static Result exception(Integer code, String msg, Object obj) {
		return new Result(code, msg, obj);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	@Override
	public String toString() {
		return "Result [code=" + code + ", msg=" + msg + ", obj=" + obj + "]";
	}

}
