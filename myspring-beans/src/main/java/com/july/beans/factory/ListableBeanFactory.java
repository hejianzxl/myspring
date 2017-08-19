package com.july.beans.factory;

import com.july.beans.MyBeanFactory;

public abstract interface ListableBeanFactory extends MyBeanFactory {

	//返回benaDefinitionCount
	public abstract int getBeanDefinitionCount();

	//返回beanDefinitionNames
	public abstract String[] getBeanDefinitionNames();
	
	//返回beanDefinitionNames（classType）
	public abstract String[] getBeanDefinitionNames(Class paramClass);
}
