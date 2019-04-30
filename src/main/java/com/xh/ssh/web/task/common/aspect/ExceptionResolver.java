package com.xh.ssh.web.task.common.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.xh.ssh.web.task.common.result.Result;
import com.xh.ssh.web.task.common.tool.LogTool;
import com.xh.ssh.web.task.common.tool.WebTool;


/**
 * <p>Title: 异常处理，对ajax类型的异常返回ajax错误，避免页面问题</p>
 * <p>Description: 只处理程序发生的异常，忽略程序种抛出出异常</p>
 * 
 * @author H.Yang
 * @date 2018年3月6日
 *
 */
@Component
public class ExceptionResolver implements HandlerExceptionResolver {

	@SuppressWarnings("unchecked")
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		// log记录异常
		LogTool.error(this.getClass(), e);
		// 非控制器请求照成的异常
		if (!(handler instanceof HandlerMethod)) {
			return new ModelAndView("error/500");
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;

		if (WebTool.isAjax(handlerMethod)) {
			Result<Object> result = new Result<Object>(null, e.getMessage());
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.setContentType("text/html;charset=UTF-8");
			return new ModelAndView(view, BeanMap.create(result));
		}

		// 页面指定状态为500，便于上层的resion或者nginx的500页面跳转，由于error/500不适合对用户展示
		// response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		return new ModelAndView("error/500").addObject("error", e.getMessage());
	}

}