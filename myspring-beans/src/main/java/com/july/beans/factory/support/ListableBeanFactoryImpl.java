package com.july.beans.factory.support;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.july.beans.MyBeanFactory;
import com.july.beans.factory.MyListableBeanFactory;

/**
 * ListableBeanFactory实现类
 * bean容器
 * @author hejian
 *
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements MyListableBeanFactory {
	
	//beanDefinition map
	private Map beanDefinitionHash = new HashMap();
	
	public ListableBeanFactoryImpl() {
		super();
	}

	public ListableBeanFactoryImpl(MyBeanFactory parentBeanFactory) {
		super(parentBeanFactory);
	}

	public Object getBean(String name) {
		Object object = null;
		AbstractBeanDefinition bd = getBeanDefinition(name);
		
		if (bd instanceof RootBeanDefinition) {
			RootBeanDefinition rbd = (RootBeanDefinition) bd;
			try {
				return rbd.getBeanClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public <T> T getBean(String name, Class<T> requiredType) {
		return null;
	}

	public boolean isSingleton(String name) {
		return false;
	}

	public <T> boolean isSingleton(Class<T> classss) {
		return false;
	}

	public String[] getAliases(String name) {
		return null;
	}

	public int getBeanDefinitionCount() {
		return 0;
	}

	public String[] getBeanDefinitionNames() {
		Set keys = beanDefinitionHash.keySet();
		String[] names = new String[keys.size()];
		Iterator itr = keys.iterator();
		int i = 0;
		while (itr.hasNext()) {
			names[i++] = (String) itr.next();
		}
		return names;
	}

	public String[] getBeanDefinitionNames(Class paramClass) {
		return null;
	}

	@Override
	protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected RootBeanDefinition getBeanDefinition(String beanName) {
		return (RootBeanDefinition) beanDefinitionHash.get(beanName);
	}

	@Override
	protected boolean containsBeanDefinition(String beanName) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 待实现 实例化单列对象 并处理InitializingBean 后置处理
	 */
	public void preInstantiateSingletons() {
		
	}
	
	/**
	 * definition bean map
	 * @param id
	 * @param beanDefinition
	 */
	public void registerBeanDefinition(String prototypeName, AbstractBeanDefinition beanDefinition) {
		beanDefinitionHash.put(prototypeName, beanDefinition);
	}

}
