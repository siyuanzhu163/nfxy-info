package com.nfxy.manager.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.nfxy.manager.AccessLog;
import com.nfxy.manager.context.RequestContext;

/**
 * 记录当前请求的相关信息到log日志中，以便后期错误分析、统计
 * @author SIYUAN
 */
public class AccessLogInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger LOGGER = LoggerFactory.getLogger(AccessLogInterceptor.class);
	
	private static final String LOGIN_PATH = "/manager/login";
	
	private static final String LOGIN_METHOD = "POST";
	
	@Override
	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Map<String, String[]> params = request.getParameterMap();
		//登录不记录密码
		if (LOGIN_PATH.equals(request.getServletPath())
				&& LOGIN_METHOD.equalsIgnoreCase(request.getMethod())) {
			params = new HashMap<String, String[]>();
			params.putAll(request.getParameterMap());
			params.remove("password");
		}
		AccessLog accessLog = new AccessLog(RequestContext.getRequestUUID(),
				RequestContext.getManager(),
				request.getServletPath(),
				request.getMethod(),
				params);
		LOGGER.info(JSON.toJSONString(accessLog));
		return true;
	}
	
}
