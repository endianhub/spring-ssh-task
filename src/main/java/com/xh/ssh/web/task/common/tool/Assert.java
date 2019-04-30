package com.xh.ssh.web.task.common.tool;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>Title: 数据数据校验</p>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @date 2018年3月16日
 * 
 */
public abstract class Assert extends org.springframework.util.Assert{

	public static void isBlank(String str, String message) {
		if (StringUtils.isBlank(str)) {
			throw new RuntimeException(message);
		}
	}

//	public static void isNull(Object object, String message) {
//		if (object == null) {
//			throw new RuntimeException(message);
//		}
//	}
}
