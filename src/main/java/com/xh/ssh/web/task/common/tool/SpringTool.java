package com.xh.ssh.web.task.common.tool;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.xh.ssh.web.task.common.exception.ResultException;


/**
 * <b>Title: Spring工具类</b>
 * <p>Description: </p>
 * 
 * @author H.Yang
 * @email xhaimail@163.com
 * @date 2018年9月5日
 */
@SuppressWarnings("all")
public class SpringTool implements ApplicationContextAware {

	// Spring应用上下文环境
	private static ApplicationContext applicationContext;

	/**
	 * <b>Title: 接口实现</b>
	 * <p>Description: </p>
	 * 
	 * @author H.Yang
	 * 
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static Object getBean(HttpServletRequest request, String beanName) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		Object bean = ctx.getBean(beanName);
		return bean;
	}

	public static <T> T getSpringBean(HttpServletRequest request, String beanName) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
		T bean = (T) ctx.getBean(beanName);
		return bean;
	}

	public static <T> T getSpringBeanThrows(String beanName) {

		if (getSpringBean(StringUtils.uncapitalize(beanName)) == null) {
			throw new ResultException("can not get bean:" + beanName);
		}
		T bean = (T) getSpringBean(StringUtils.uncapitalize(beanName));
		return bean;
	}

	public static <T> T getSpringBean(String beanName) {
		T bean = null;
		try {
			if (applicationContext.getBean(StringUtils.uncapitalize(beanName)) != null) {
				bean = (T) applicationContext.getBean(StringUtils.uncapitalize(beanName));
			}
		} catch (Exception e) {
			bean = null;
		}

		return bean;
	}

	public static <T> T getSpringBean(Class<T> t) {
		String beanName = StringUtils.uncapitalize(t.getSimpleName());
		if (applicationContext.getBean(beanName) == null) {
			return null;
		}
		T bean = (T) applicationContext.getBean(beanName);
		return bean;
	}

	public static <T> T getSpringByBean(Class<T> t) {
		if (applicationContext.getBean(t) == null) {
			return null;
		}
		T bean = (T) applicationContext.getBean(t);
		return bean;
	}

	public static Object getSpringBeanByXml(String springXmlPath, String beanName) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { springXmlPath });
		BeanFactory factory = (BeanFactory) context;

		Object bean = factory.getBean(beanName);
		return bean;
	}

	public static Object getSpringBeanByXml(String[] xmlPath, String beanName) {
		ApplicationContext context = new ClassPathXmlApplicationContext(xmlPath);
		BeanFactory factory = (BeanFactory) context;

		Object bean = factory.getBean(beanName);
		return bean;
	}

	public static <T> T getBeanByXml(String springXmlPath, String beanName) {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { springXmlPath });
		BeanFactory factory = (BeanFactory) context;

		T bean = (T) factory.getBean(beanName);
		return bean;
	}

	public static <T> T getBeanByXml(String springXmlPath, Class<T> t) {
		String beanName = StringUtils.uncapitalize(t.getSimpleName());
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { springXmlPath });
		BeanFactory factory = (BeanFactory) context;
		T bean = (T) factory.getBean(beanName);
		return bean;
	}

}
