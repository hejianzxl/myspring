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

	/*public Object getBean(String name) {
		Object object = null;
		AbstractBeanDefinition bd = getBeanDefinition(name);
		
		if (bd instanceof RootBeanDefinition) {
			RootBeanDefinition rbd = (RootBeanDefinition) bd;
			try {
				
				if(rbd.isSingleton()) {
					System.out.println("beanDefinitionHash get bean" + name);
					beanDefinitionHash.get(name);
				}
				return rbd.getBeanClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}*/


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

	@Override
	protected RootBeanDefinition getBeanDefinition(String beanName) {
		return (RootBeanDefinition) beanDefinitionHash.get(beanName);
	}

	/**
	 * 待实现 实例化单列对象 并处理InitializingBean 后置处理
	 */
	public void preInstantiateSingletons() {
		String[] beanNames = getBeanDefinitionNames();
		for (int i = 0; i < beanNames.length; i++) {
			AbstractBeanDefinition bd = getBeanDefinition(beanNames[i]);
			if (bd.isSingleton()) {
				Object singleton = getBean(beanNames[i]);
	 			logger.debug("Instantiated singleton: " + singleton);
			}
		}
	}
	
	/**
	 * definition bean map
	 * @param id
	 * @param beanDefinition
	 */
	public void registerBeanDefinition(String prototypeName, AbstractBeanDefinition beanDefinition) {
		beanDefinitionHash.put(prototypeName, beanDefinition);
	}

	public int getBeanDefinitionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String[] getBeanDefinitionNames(Class type) {
		// TODO Auto-generated method stub
		return null;
	}
}
