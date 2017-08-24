package org.myspring.beans;

import com.july.beans.factory.xml.XmlBeanFactory;
import com.test.bean.dao.UserDao;
import com.test.bean.service.UserService;

public class XmlBeanFactoryTest {
	
	public static void main(String[] args) throws Exception {
		XmlBeanFactory beanFactory = new XmlBeanFactory("applicationContext.xml");
		String[] result = beanFactory.getBeanDefinitionNames();
		UserDao userDao = (UserDao) beanFactory.getBean("userDao");
		System.out.println("id " + userDao.getId() + "  name " + userDao.getName());
		UserService userService = (UserService) beanFactory.getBean("userService");
		System.out.println(userService + "  getName: " + userService.getName());
	}
}
