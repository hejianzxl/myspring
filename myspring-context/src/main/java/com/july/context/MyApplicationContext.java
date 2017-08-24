package com.july.context;

import java.io.IOException;
import java.io.InputStream;

import com.july.beans.factory.MyListableBeanFactory;

/**
 * applicationContext上下文
 * @author hejian
 *
 */
public interface MyApplicationContext extends MyListableBeanFactory{
	
	/**
	 * 获取父Context
	 * @return
	 */
	MyApplicationContext getParent();
	
	
	/**
	 * 返回启动时间
	 * @return
	 */
	long getStartupDate();
	
	//返回context options
	//ContextOptions getOptions();
	
	/**
	 * refresh context
	 */
	void refresh();
	
	InputStream getResourceAsStream(String location) throws IOException;
	
	String getResourceBasePath();
	
	void shareObject(String key, Object o);
	
	Object sharedObject(String key);
	
	Object removeSharedObject(String key);

}
