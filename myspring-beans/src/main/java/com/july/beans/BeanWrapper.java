package com.july.beans;

import java.beans.PropertyVetoException;

public interface BeanWrapper {

	String NESTED_PROPERTY_SEPARATOR = ".";
	
	void setPropertyValue(String propertyName, Object value) throws PropertyVetoException;
	
	void setPropertyValue(PropertyValue pv) throws PropertyVetoException;
	
	Object getPropertyValue(String propertyName);
	
	void setPropertyValues(PropertyValues pvs) throws Exception;
	
	Object getWrappedInstance();
	
	void setWrappedInstance(Object obj) throws Exception;
	
	void newWrappedInstance() throws Exception;
	
	BeanWrapper newWrapper(Object obj) throws Exception;
	
	Class getWrappedClass();
	
	Object invoke(String methodName, Object[] args) throws Exception;
	
}
