package com.july.beans;

/**
 * beanFactory工厂接口
 * @author hejian
 *
 */
public interface MyBeanFactory {
	
	//获取bean
	Object getBean(String name);
	
	<T> T getBean(String name,Class<T> requiredType);
	
	//是否单列
	boolean isSingleton(String name);
	
	//单列
	<T> boolean isSingleton(Class<T> classss);
	
	//获取别名
	String[] getAliases(String name);
}
