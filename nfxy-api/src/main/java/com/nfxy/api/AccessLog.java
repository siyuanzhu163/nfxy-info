package com.nfxy.api;

import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 本次request请求的相关信息
 * @author SIYUAN
 */
public class AccessLog {
	
	@JSONField(ordinal=0)
	private String uuid;
	
	@JSONField(ordinal=1)
	private String servletPath;
	
	@JSONField(ordinal=2)
	private String method;
	
	@JSONField(ordinal=3)
	private Map<java.lang.String,java.lang.String[]> params;
	
	public AccessLog(String uuid, String servletPath,
			String method, Map<String, String[]> params) {
		this.uuid = uuid;
		this.servletPath = servletPath;
		this.method = method;
		this.params = params;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<java.lang.String, java.lang.String[]> getParams() {
		return params;
	}

	public void setParams(Map<java.lang.String, java.lang.String[]> params) {
		this.params = params;
	}
	
}
