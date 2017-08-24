package com.july.beans.factory.support;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.july.beans.BeanPropertyEditor;
import com.july.beans.MyBeanFactory;
import com.july.beans.PropertyValue;
import com.july.beans.PropertyValues;

public abstract class AbstractBeanFactory implements MyBeanFactory {

	public static final String	FACTORY_BEAN_PREFIX	= "&";
	private MyBeanFactory		parentBeanFactory;
	private final Map			singletonCache		= new ConcurrentHashMap<String, Object>();
	protected final Log			logger				= LogFactory.getLog(getClass());
	protected String			defaultParentBean;
	private final Map			aliasMap			= new ConcurrentHashMap<String, String>();

	public AbstractBeanFactory() {
	}

	public AbstractBeanFactory(MyBeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}

	private Object createBean(String name, Map newlyCreatedBeans) {
		/*
		 * if (newlyCreatedBeans == null) { newlyCreatedBeans = new HashMap(); }
		 * Object bean = getBeanWrapperForNewInstance(name,
		 * newlyCreatedBeans).getWrappedInstance();
		 * callLifecycleMethodsIfNecessary(bean, name);
		 */
		return null;
	}

	public Object getBean(String name) {
		return getBeanInternal(name, null);
	}

	public <T> T getBean(String name, Class<T> requiredType) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleton(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> boolean isSingleton(Class<T> classss) {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getAliases(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取单列bean对象
	 * 
	 * @param name
	 * @param object
	 * @return
	 */
	private Object getBeanInternal(String name, Map newlyCreatedBeans) {
		// 不清楚干啥的，未研究
		if (newlyCreatedBeans != null && newlyCreatedBeans.containsKey(name)) {
			return newlyCreatedBeans.get(name);
		}
		Object beanObject = null;
		try {
			AbstractBeanDefinition bd = getBeanDefinition(transformedBeanName(name));
			PropertyValues pvs = bd.getPropertyValues();
			if (bd instanceof RootBeanDefinition) {
				RootBeanDefinition rbd = (RootBeanDefinition) bd;
				// 简单实现单列对象创建 后续改造 TODO
				if (rbd.isSingleton()) {
					beanObject = singletonCache.get(transformedBeanName(name));
					if (null == beanObject) {
						beanObject = rbd.newBeanWrapper();
						applyPropertyValues(bd, beanObject, pvs, name);
						singletonCache.put(transformedBeanName(name), beanObject);
						return beanObject;
					}
				} else {
					beanObject = rbd.newBeanWrapper();
				}
			} else if (bd instanceof ChildBeanDefinition) {
				ChildBeanDefinition ibd = (ChildBeanDefinition) bd;
			}
			// return bd.isSingleton() ? getSharedInstance(name,
			// newlyCreatedBeans) : createBean(name, newlyCreatedBeans);
			// set property
			applyPropertyValues(bd, beanObject, pvs, name);
		} catch (Exception ex) {
			// not found -> check parent
			if (this.parentBeanFactory != null)
				return this.parentBeanFactory.getBean(name);
			logger.error("beanInternal is error ", ex);
		}
		return beanObject;
	}

	private void applyPropertyValues(AbstractBeanDefinition beanDefinition, Object beanObject, PropertyValues pvs,
			String name) {
		if (null != pvs) {
			PropertyValue[] propertyValueArray = pvs.getPropertyValues();
			if (null != propertyValueArray && propertyValueArray.length > 0) {
				for (PropertyValue propertyValue : propertyValueArray) {
					try {
						Method setMethod = this.getMethod(beanObject.getClass(), propertyValue);
						if (null == setMethod) {
							logger.error("get bean getter and setter method is error is " + propertyValue);
							return;
						}

						int nParams = setMethod.getParameterTypes().length;
						Class<?> propertyType = setMethod.getParameterTypes()[nParams - 1];
						setMethod.setAccessible(Boolean.TRUE);

						if (propertyType.isAssignableFrom(String.class)) {
							setMethod.invoke(beanObject, propertyValue.getValue());
						} else {
							logger.info("findCustomEditor propertyType " + propertyType + " value "
									+ propertyValue.getValue());
							PropertyEditor pe = findCustomEditor(propertyType, propertyValue.getValue());
							pe.setAsText((String) propertyValue.getValue());
							setMethod.invoke(beanObject, pe.getValue());
						}

						/*
						 * if(editorManager instanceof PropertyEditorManager) {
						 * PropertyEditor propertyEditor =
						 * ((PropertyEditorManager)
						 * editorManager).findEditor(propertyType);
						 * setMethod.setAccessible(Boolean.TRUE);
						 * setMethod.invoke(beanObject, ); }else {
						 * logger.error("not find PropertyEditor"); }
						 */
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * 获取属性编辑器
	 * 
	 * @param requiredType
	 * @param value
	 * @return
	 */
	private PropertyEditor findCustomEditor(Class<?> requiredType, Object value) {
		BeanPropertyEditor beanPropertyEditor = (BeanPropertyEditor) singletonCache.get("beanPropertyEditor");
		PropertyEditor pe = beanPropertyEditor.doFindCustomEditor(requiredType, null);
		return pe;
	}

	/**
	 * 获取set method
	 * 
	 * @param class1
	 * @param propertyValue
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	private Method getMethod(Class<? extends Object> class1, PropertyValue propertyValue)
			throws NoSuchMethodException, SecurityException {
		Method[] methods = class1.getDeclaredMethods();
		Method setMethod = null;
		if (null != methods && methods.length > 0) {
			String methodValeus = "set" + propertyValue.getName().substring(0, 1).toUpperCase()
					+ propertyValue.getName().substring(1, propertyValue.getName().length());
			for (Method method : methods) {
				if (method.getName().equals(methodValeus)) {
					return method;
				}
			}
		}
		return setMethod;
	}

	/**
	 * 别名
	 * 
	 * @param name
	 * @return
	 */
	private String transformedBeanName(String name) {
		if (name.startsWith(FACTORY_BEAN_PREFIX)) {
			name = name.substring(FACTORY_BEAN_PREFIX.length());
		}
		// Handle aliasing
		String canonicalName = (String) this.aliasMap.get(name);
		return canonicalName != null ? canonicalName : name;
	}

	// 初始化常用的bean
	public void instantiateSupplementaryBean() {
		singletonCache.put("propertyEditorManager", new PropertyEditorManager());
		singletonCache.put("beanPropertyEditor", new BeanPropertyEditor());
	}

	protected abstract RootBeanDefinition getBeanDefinition(String beanName);

}
