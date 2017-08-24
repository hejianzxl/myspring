package com.july.web.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyContextLoaderListener implements ServletContextListener{

	public void contextInitialized(ServletContextEvent event) {
		ContextLoader.initContext(event.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
	}

}
