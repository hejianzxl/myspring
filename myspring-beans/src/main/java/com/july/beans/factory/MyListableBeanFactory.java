package com.july.beans.factory;

import com.july.beans.MyBeanFactory;

/**
 * 扩展beanFactory接口
 * @author hejian
 *
 */
public interface MyListableBeanFactory extends MyBeanFactory{
	
	/**
	 * 获取beanDefinition数量
	 * @return
	 */
	int getBeanDefinitionCount();
	
	/**
	 * 获取beanDefinitionNames
	 * @return
	 */
	String[] getBeanDefinitionNames();
	
	String[] getBeanDefinitionNames(Class type);

}
