package com.july.beans.factory.support;

import com.july.beans.PropertyValues;

public class AbstractBeanDefinition {
	private boolean			singleton;
	private PropertyValues	pvs;

	protected AbstractBeanDefinition(PropertyValues pvs, boolean singleton) {
		this.pvs = pvs;
		this.singleton = singleton;
	}

	protected AbstractBeanDefinition() {
		this.singleton = true;
	}

	public final boolean isSingleton() {
		return this.singleton;
	}
	
	/**
	 * propertyValues setter
	 * @param pvs
	 */
	public void setPropertyValues(PropertyValues pvs) {
		this.pvs = pvs;
	}
	
	/***
	 * propertyValues getter
	 * @return
	 */
	public PropertyValues getPropertyValues() {
		return this.pvs;
	}
}
