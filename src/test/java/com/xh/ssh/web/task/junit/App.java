package com.xh.ssh.web.task.junit;

import com.xh.ssh.web.task.common.redis.JedisClientUtils;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2019年7月17日
 */
public class App {

	public static void main(String[] args) {

		String json = (String) JedisClientUtils.get("WebTask");
		System.out.println(json);

	}
}
