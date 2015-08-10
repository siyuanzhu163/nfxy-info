package com.nfxy.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.template.SimpleHash;

/**
 * 在View的Model中加入一些常用的数据
 * @author SIYUAN
 *
 */
public class FreeMarkerViewExt extends FreeMarkerView {
	
	public static final String KEY_CONTEXT_PATH = "ContextPath";
	
	public static final String KEY_COOKIES = "Cookies";
	
	@Override
	protected SimpleHash buildTemplateModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) {
		
		model.put(KEY_CONTEXT_PATH, request.getContextPath());
		
		Map<String, String> cookiesMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookiesMap.put(cookie.getName(), cookie.getValue());
			}
		}
		model.put(KEY_COOKIES, cookiesMap);
		return super.buildTemplateModel(model, request, response);
	}
	
}
