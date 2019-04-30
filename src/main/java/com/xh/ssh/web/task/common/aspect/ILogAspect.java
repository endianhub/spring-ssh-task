package com.xh.ssh.web.task.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <b>Title: AOP 日志</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月23日
 */
public interface ILogAspect {

	public void before(JoinPoint point);

	public Object around(ProceedingJoinPoint point) throws Throwable;

	public void after(JoinPoint point);

	public void afterThrowing(JoinPoint joinPoint, Exception exception);

}
