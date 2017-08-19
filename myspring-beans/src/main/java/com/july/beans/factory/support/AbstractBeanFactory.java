package com.july.beans.factory.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.july.beans.MyBeanFactory;

public abstract class AbstractBeanFactory implements MyBeanFactory {

	public static final String	FACTORY_BEAN_PREFIX	= "&";
	private MyBeanFactory		parentBeanFactory;
	private Map					sharedInstanceCache	= new ConcurrentHashMap<String, Object>();
	protected final Log			logger				= LogFactory.getLog(getClass());
	protected String			defaultParentBean;
	private Map					aliasMap			= new ConcurrentHashMap<String, String>();

	public AbstractBeanFactory() {
	}

	public AbstractBeanFactory(MyBeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}
	
	protected abstract Object createBean(String beanName, RootBeanDefinition mbd, Object[] args);
	protected abstract RootBeanDefinition getBeanDefinition(String beanName);
	protected abstract boolean containsBeanDefinition(String beanName);


}
