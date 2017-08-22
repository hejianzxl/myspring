package com.july.beans.factory.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.july.beans.MyBeanFactory;
import com.july.beans.PropertyValue;
import com.july.beans.PropertyValues;

public abstract class AbstractBeanFactory implements MyBeanFactory {

	public static final String	FACTORY_BEAN_PREFIX	= "&";
	private MyBeanFactory		parentBeanFactory;
	private Map					singletonCache	= new ConcurrentHashMap<String, Object>();
	protected final Log			logger				= LogFactory.getLog(getClass());
	protected String			defaultParentBean;
	private Map					aliasMap			= new ConcurrentHashMap<String, String>();

	public AbstractBeanFactory() {
	}

	public AbstractBeanFactory(MyBeanFactory parentBeanFactory) {
		this.parentBeanFactory = parentBeanFactory;
	}
	
	private Object createBean(String name, Map newlyCreatedBeans) {
		/*if (newlyCreatedBeans == null) {
			newlyCreatedBeans = new HashMap();
		}
		Object bean = getBeanWrapperForNewInstance(name, newlyCreatedBeans).getWrappedInstance();
		callLifecycleMethodsIfNecessary(bean, name);*/
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
	 * @param name
	 * @param object
	 * @return
	 */
	private Object getBeanInternal(String name, Map newlyCreatedBeans) {
		
		//不清楚干啥的，未研究
		if (newlyCreatedBeans != null && newlyCreatedBeans.containsKey(name)) {
			return newlyCreatedBeans.get(name);
		}
		Object beanObject = null;
		try {
			AbstractBeanDefinition bd = getBeanDefinition(transformedBeanName(name));
			if (bd instanceof RootBeanDefinition) {
				RootBeanDefinition rbd = (RootBeanDefinition) bd;
				//简单实现单列对象创建  后续改造 TODO
				if(rbd.isSingleton()) {
					 beanObject = singletonCache.get(transformedBeanName(name));
					if(null == beanObject) {
						beanObject = rbd.newBeanWrapper();
						singletonCache.put(transformedBeanName(name), beanObject);
					}
					return beanObject;
				}else {
					beanObject = rbd.newBeanWrapper();
				}
			}
			else if (bd instanceof ChildBeanDefinition) {
				ChildBeanDefinition ibd = (ChildBeanDefinition) bd;
			}
			//return bd.isSingleton() ? getSharedInstance(name, newlyCreatedBeans) : createBean(name, newlyCreatedBeans);
			
			//set property
			PropertyValues pvs = bd.getPropertyValues();
			applyPropertyValues(bd,beanObject, pvs, name);
		}
		catch (Exception ex) {
			// not found -> check parent
			if (this.parentBeanFactory != null)
				return this.parentBeanFactory.getBean(name);
			logger.error("beanInternal is error ",ex);
		}
		return beanObject;
	}
	
	private void applyPropertyValues(AbstractBeanDefinition beanDefinition,Object beanObject, PropertyValues pvs, String name) {
		if(null != pvs) {
			PropertyValue[] propertyValueArray = pvs.getPropertyValues();
			if(null != propertyValueArray && propertyValueArray.length > 0) {
				for(PropertyValue propertyValue : propertyValueArray) {
					System.out.println(propertyValue);
					Class beanClass = beanObject.getClass();
					//方法名称   方法参数类型
					try {
						Method setMethod = beanClass.getMethod("set" + propertyValue.getName().substring(0, 1).toUpperCase() + propertyValue.getName().substring(1, propertyValue.getName().length()), String.class);
						setMethod.setAccessible(Boolean.TRUE);
						setMethod.invoke(beanObject, propertyValue.getValue());
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	/**
	 * 别名
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

	protected abstract RootBeanDefinition getBeanDefinition(String beanName);

}
