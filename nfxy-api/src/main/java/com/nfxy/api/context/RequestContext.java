package com.nfxy.api.context;

/**
 * 在线程上下文中记录当前请求中的一些信息
 * @author SIYUAN
 */
public class RequestContext {
	
	private static ThreadLocal<String> requestUUID = new ThreadLocal<String>();
	
	public static String getRequestUUID() {
		return requestUUID.get();
	}

	public static void setRequestUUID(String uuid) {
		requestUUID.set(uuid);
	}

}
