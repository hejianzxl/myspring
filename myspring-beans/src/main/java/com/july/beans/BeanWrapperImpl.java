package com.july.beans;

import java.beans.PropertyVetoException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.july.beans.propertyeditors.CustomBooleanEditor;
import com.july.beans.propertyeditors.CustomNumberEditor;

public class BeanWrapperImpl implements BeanWrapper {
	
	public static final boolean DEFAULT_EVENT_PROPAGATION_ENABLED = false;

	private static final Log logger = LogFactory.getLog(BeanWrapperImpl.class);
	
	private static final Map defaultEditors = new HashMap();
	
	/** Should we propagate events to listeners? */
	private boolean	eventPropagationEnabled = DEFAULT_EVENT_PROPAGATION_ENABLED;
	
	/** The wrapped object */
	private Object object;
	
	private Map nestedBeanWrappers;
	
	private Map customEditors;
	
	static {
		defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, Boolean.FALSE));
		defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, Boolean.FALSE));
		defaultEditors.put(float.class, new CustomNumberEditor(Float.class, Boolean.FALSE));
		defaultEditors.put(Float.class, new CustomNumberEditor(Float.class, Boolean.FALSE));
		defaultEditors.put(byte.class, new CustomNumberEditor(Byte.class, Boolean.FALSE));
		defaultEditors.put(Byte.class, new CustomNumberEditor(Byte.class, Boolean.FALSE));
		defaultEditors.put(short.class, new CustomNumberEditor(Short.class, Boolean.FALSE));
		defaultEditors.put(Short.class, new CustomNumberEditor(Short.class, Boolean.FALSE));
		defaultEditors.put(long.class, new CustomNumberEditor(Long.class, Boolean.FALSE));
		defaultEditors.put(Long.class, new CustomNumberEditor(Long.class, Boolean.FALSE));
		defaultEditors.put(double.class, new CustomNumberEditor(Double.class, Boolean.FALSE));
		defaultEditors.put(Double.class, new CustomNumberEditor(Double.class, Boolean.FALSE));
		defaultEditors.put(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, Boolean.FALSE));
		defaultEditors.put(BigInteger.class, new CustomNumberEditor(BigInteger.class, Boolean.FALSE));
		defaultEditors.put(boolean.class, new CustomBooleanEditor(false));
		defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));

	}
	
	public BeanWrapperImpl(Object object) throws Exception {
		this(object, DEFAULT_EVENT_PROPAGATION_ENABLED);
	}
	
	public BeanWrapperImpl(Class clazz) throws Exception {
		setObject(clazz.newInstance());
	}
	
	public BeanWrapperImpl(Object object, boolean eventPropagationEnabled) throws Exception {
		this.eventPropagationEnabled = eventPropagationEnabled;
		setObject(object);
	}
	
	/*public BeanWrapperImpl(Class clazz) throws BeansException {
		this.cachedIntrospectionResults = CachedIntrospectionResults.forClass(clazz);
		setObject(BeanUtils.instantiateClass(clazz));
	}*/
	
	public BeanWrapper newWrapper(Object obj) throws Exception {
		return new BeanWrapperImpl(obj);
	}
	
	private void setObject(Object object) throws Exception {
		if (object == null)
			throw new RuntimeException("Cannot set BeanWrapperImpl target to a null object", null);
		this.object = object;
	}
	
	public void setWrappedInstance(Object object) throws Exception {
		setObject(object);
	}
	
	public void newWrappedInstance() throws Exception {
		this.object = getWrappedClass().newInstance();
	}

	public Class getWrappedClass() {
		return object.getClass();
	}
	
	public Object getWrappedInstance() {
		return object;
	}
	
	public void setPropertyValue(String propertyName, Object value) throws PropertyVetoException {
		setPropertyValue(new PropertyValue(propertyName, value));
	}

	public void setPropertyValue(PropertyValue pv) throws PropertyVetoException {
		// TODO Auto-generated method stub
		
	}

	public Object getPropertyValue(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPropertyValues(PropertyValues pvs) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public Object invoke(String methodName, Object[] args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
