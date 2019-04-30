package com.xh.ssh.web.task.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>Title: </b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月24日
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AroundAspect {

	/**
	 * <b>Title: 是否显示日志</b>
	 * <p>Description: </p>
	 */
	boolean value() default true;

	/**
	 * <b>Title: 是否显示结果</b>
	 * <p>Description: </p>
	 */
	boolean resultSet() default false;

	boolean isSave() default false;
}
