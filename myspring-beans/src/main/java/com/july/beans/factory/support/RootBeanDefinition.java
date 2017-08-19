package com.july.beans.factory.support;

import com.july.beans.PropertyValues;

public class RootBeanDefinition extends AbstractBeanDefinition {

	private Class clazz;

	public RootBeanDefinition(Class clazz, PropertyValues pvs, boolean singleton) {
		super(pvs, singleton);
		this.clazz = clazz;
	}

	protected RootBeanDefinition() {
	}

	protected void setBeanClass(Class clazz) {
		this.clazz = clazz;
	}

	public void setBeanClassName(String classname) throws ClassNotFoundException {
		this.clazz = Class.forName(classname);
	}

	public final Class getBeanClass() {
		return this.clazz;
	}

	protected Object newBeanWrapper() throws InstantiationException, IllegalAccessException {
		return clazz.newInstance(); 
	}
}
