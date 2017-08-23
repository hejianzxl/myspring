package org.myspring.beans;

import com.july.beans.factory.xml.XmlBeanFactory;

public class XmlBeanFactoryTest {
	
	public static void main(String[] args) throws Exception {
		XmlBeanFactory beanFactory = new XmlBeanFactory("applicationContext.xml");
		String[] result = beanFactory.getBeanDefinitionNames();
		System.out.println(result.length);
	}
}
