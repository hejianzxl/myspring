package com.july.beans.factory.support;

import com.july.beans.MyBeanFactory;
import com.july.beans.factory.ListableBeanFactory;


/**
 * ListableBeanFactory实现类
 * bean容器
 * @author hejian
 *
 */
public class ListableBeanFactoryImpl extends AbstractBeanFactory implements ListableBeanFactory {

	public ListableBeanFactoryImpl() {
	}

	public ListableBeanFactoryImpl(MyBeanFactory parentBeanFactory) {
		super(parentBeanFactory);
	}

	public Object getBean(String name) {
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
		return null;
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
		// TODO Auto-generated method stub
		return null;
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

}
