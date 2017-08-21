package com.july.beans.factory.support;

import com.july.beans.PropertyValues;

/**
 * bean子节点属性对象
 * @author hejian
 *
 */
public class ChildBeanDefinition extends AbstractBeanDefinition{
	
	private String parentName;
	
	public ChildBeanDefinition(String parentName, PropertyValues pvs, boolean singleton) {
		super(pvs, singleton);
		this.parentName = parentName;
	}
}
