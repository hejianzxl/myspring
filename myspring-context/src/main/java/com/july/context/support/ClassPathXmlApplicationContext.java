package com.july.context.support;

import java.io.IOException;
import java.io.InputStream;

import com.july.context.MyApplicationContext;

public class ClassPathXmlApplicationContext extends FileSystemXmlApplicationContext{

	public ClassPathXmlApplicationContext(String locations) throws IOException {
		super(locations);
	}
	
	public ClassPathXmlApplicationContext(String[] locations) throws IOException {
		super(locations);
	}

	protected MyApplicationContext createParentContext(String[] locations) throws IOException {
		return new ClassPathXmlApplicationContext(locations);
	}
	
	protected InputStream getResourceByPath(String path) throws IOException {
		if (!path.startsWith("/")) {
			// always use root,
			// as loading relative to this class' package doesn't make sense
			path = "/" + path;
		}
		return getClass().getResourceAsStream(path);
	}
	
	public String getResourceBasePath() {
		return null;
	}
	
}
