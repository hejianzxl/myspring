package com.july.beans.factory.support;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.july.beans.BeanPropertyEditor;
import com.july.beans.BeanWrapper;
import com.july.beans.MyBeanFactory;
import com.july.beans.PropertyValue;
import com.july.beans.PropertyValues;
import com.july.beans.factory.InitializingBean;
import com.july.beans.utils.StringUtils;

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
		if (newlyCreatedBeans == null) {
			newlyCreatedBeans = new HashMap();
		}
		Object bean = null;
		try {
			bean = getBeanWrapperForNewInstance(name, newlyCreatedBeans).getWrappedInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		//InitializingBean 后置处理
		callLifecycleMethodsIfNecessary(bean, name);
		return bean;
	}

	private void callLifecycleMethodsIfNecessary(Object bean, String name) {
		if (bean instanceof InitializingBean) {
		}
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

	private BeanWrapper getBeanWrapperForNewInstance(String name, Map newlyCreatedBeans) throws InstantiationException, IllegalAccessException {
		logger.debug("getBeanWrapperForNewInstance (" + name + ")");
		AbstractBeanDefinition bd = getBeanDefinition(name);
		logger.debug("getBeanWrapperForNewInstance definition is: " + bd);
		BeanWrapper instanceWrapper = null;
			PropertyValues pvs = bd.getPropertyValues();
			if (bd instanceof RootBeanDefinition) {
				RootBeanDefinition rbd = (RootBeanDefinition) bd;
				try {
					instanceWrapper = rbd.getBeanWrapperForNewInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (bd instanceof ChildBeanDefinition) {
				ChildBeanDefinition ibd = (ChildBeanDefinition) bd;
				if (newlyCreatedBeans == null) {
					newlyCreatedBeans = new HashMap();
				}
				instanceWrapper = getBeanWrapperForNewInstance(ibd.getParentName(), newlyCreatedBeans);
			}
			// return bd.isSingleton() ? getSharedInstance(name,
			newlyCreatedBeans.put(name, instanceWrapper.getWrappedInstance());
			// set property
			applyPropertyValues(bd, instanceWrapper.getWrappedInstance(), pvs, name);
			return instanceWrapper;
			
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

		try {
			AbstractBeanDefinition bd = getBeanDefinition(transformedBeanName(name));
			return bd.isSingleton() ? getSharedInstance(name, newlyCreatedBeans) : createBean(name, newlyCreatedBeans);
		} catch (Exception ex) {
			// not found -> check parent
			if (this.parentBeanFactory != null)
				return this.parentBeanFactory.getBean(name);
			logger.error("beanInternal is error ", ex);
		}
		return null;
	}

	/**
	 * 获取单列bean
	 * 
	 * @param pname
	 * @param newlyCreatedBeans
	 * @return
	 */
	private Object getSharedInstance(String pname, Map newlyCreatedBeans) {
		// 获取beanName
		String name = transformedBeanName(pname);
		if (StringUtils.isEmpty(name))
			logger.info("not find bean name in transformed");

		Object beanInstance = this.singletonCache.get(name);
		if (beanInstance == null) {
			logger.info("Cached shared instance of Singleton bean '" + name + "'");
			if (newlyCreatedBeans == null) {
				newlyCreatedBeans = new HashMap();
			}
			beanInstance = createBean(name, newlyCreatedBeans);
			this.singletonCache.put(name, beanInstance);
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Returning cached instance of Singleton bean '" + name + "'");
		}
		return beanInstance;
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
							if(null == pe) {
								//简单实现
								Object refBean = singletonCache.get(propertyValue.getValue());
								if(null == refBean) {
									RootBeanDefinition refBd = this.getBeanDefinition((String) propertyValue.getValue());
									refBean = refBd.newBeanWrapper().getWrappedInstance();
									if(refBd.isSingleton()) {
										singletonCache.put(propertyValue.getValue(), refBean);
									}
									applyPropertyValues(refBd, refBean, refBd.getPropertyValues(), (String)propertyValue.getValue());
								}
								setMethod.invoke(beanObject, refBean);
							}else {
								pe.setAsText((String) propertyValue.getValue());
								setMethod.invoke(beanObject, pe.getValue());
							}
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
					} catch (Exception e) {
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
