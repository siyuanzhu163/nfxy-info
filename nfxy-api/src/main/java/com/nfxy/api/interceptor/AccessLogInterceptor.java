package com.nfxy.api.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.nfxy.api.AccessLog;
import com.nfxy.api.context.RequestContext;

/**
 * 记录当前请求的相关信息到log日志中，以便后期错误分析、统计
 * @author SIYUAN
 */
public class AccessLogInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccessLogInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		@SuppressWarnings("unchecked")
		AccessLog accessLog = new AccessLog(RequestContext.getRequestUUID(),
				request.getServletPath(),
				request.getMethod(),
				request.getParameterMap());
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(JSON.toJSONString(accessLog));
		}
		return true;
	}
	
}
