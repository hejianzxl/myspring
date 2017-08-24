package com.july.context;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends MyApplicationContext{
	
	/** 
	 * Context attribute to bind root WebApplicationContext to on successful startup.
	 */
	String WEB_APPLICATION_CONTEXT_ATTRIBUTE_NAME = WebApplicationContext.class + ".ROOT";
	
	void setServletContext(ServletContext servletContext) throws Exception;
	
	
	/** 
	 * 获取ServlerContext(Java定义的标准接口 各个serve厂商实现此接口)
	 * Return the standard Servlet API ServletContext for this application.
	 */
	ServletContext getServletContext();
	
	
}
