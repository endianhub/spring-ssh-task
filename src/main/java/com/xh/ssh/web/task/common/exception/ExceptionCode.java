package com.xh.ssh.web.task.common.exception;

/**
 * <p>Title: 异常码</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @QQ 1033542070
 * @date 2018年3月17日
 */
public enum ExceptionCode {

	/** 成功 (200, "操作成功") */
	SUCCESS(200, "操作成功"),
	/** 请求报文语法错误或参数错误 (400, "无效参数！缺少参数！参数格式不匹配！参数非法！") **/
	PARAMETER_ERROR(400, "无效参数！缺少参数！参数格式不匹配！参数非法！"),
	/** 请求资源被拒绝 (4010, "非法请求！无操作权限！上传下载失败！") **/
	FORBIDDEN_ERROR(403, "非法请求！无操作权限！上传下载失败！"),
	/** 无法找到请求资源 (404, "无法找到请求资源！") **/
	NOT_FOUND(404, "无法找到请求资源！"),
	/** 服务器故障或Web应用故障 (500, "服务器异常！") **/
	INTERNAL_SERVER_ERROR(500, "服务器异常！"),
	/** 数据库异常 (3306, "数据库操作异常！数据异常！查询异常！") **/
	DB_ERROR(3306, "数据库操作异常！数据异常！查询异常！");

	public final Integer code;
	public final String msg;

	ExceptionCode(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ExceptionCode getExceptionCode(final String code) {
		try {
			return ExceptionCode.valueOf(code);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return INTERNAL_SERVER_ERROR;
	}
}
