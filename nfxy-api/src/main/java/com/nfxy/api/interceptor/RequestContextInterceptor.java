package com.nfxy.api.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.nfxy.api.context.RequestContext;

/**
 * 初始化RequestContext中的值
 * @author SIYUAN
 */
public class RequestContextInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		RequestContext.setRequestUUID(UUID.randomUUID().toString());
		return true;
	}
	
}
