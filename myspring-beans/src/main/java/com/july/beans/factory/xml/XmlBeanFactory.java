package com.july.beans.factory.xml;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.july.beans.MutablePropertyValues;
import com.july.beans.PropertyValue;
import com.july.beans.PropertyValues;
import com.july.beans.factory.support.AbstractBeanDefinition;
import com.july.beans.factory.support.ChildBeanDefinition;
import com.july.beans.factory.support.ListableBeanFactoryImpl;
import com.july.beans.factory.support.RootBeanDefinition;
import com.july.beans.utils.StringUtils;

public class XmlBeanFactory extends ListableBeanFactoryImpl{
	
	private static final String TRUE_ATTRIBUTE_VALUE = "true";

	private static final String BEAN_ELEMENT = "bean";

	private static final String CLASS_ATTRIBUTE = "class";

	private static final String PARENT_ATTRIBUTE = "parent";

	private static final String ID_ATTRIBUTE = "id";

	private static final String NAME_ATTRIBUTE = "name";

	private static final String SINGLETON_ATTRIBUTE = "singleton";

	private static final String DISTINGUISHED_VALUE_ATTRIBUTE = "distinguishedValue";

	private static final String NULL_DISTINGUISHED_VALUE = "null";

	private static final String PROPERTY_ELEMENT = "property";

	private static final String REF_ELEMENT = "ref";

	private static final String LIST_ELEMENT = "list";

	private static final String MAP_ELEMENT = "map";

	private static final String KEY_ATTRIBUTE = "key";

	private static final String ENTRY_ELEMENT = "entry";

	private static final String BEAN_REF_ATTRIBUTE = "bean";

	private static final String EXTERNAL_REF_ATTRIBUTE = "external";

	private static final String VALUE_ELEMENT = "value";

	private static final String PROPS_ELEMENT = "props";

	private static final String PROP_ELEMENT = "prop";

	public XmlBeanFactory(InputStream is) {
		loadBeanDefinitions(is);
	}

	private void loadBeanDefinitions(InputStream is) {
		//创建document解析工厂
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //创建document解析器
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document document = db.parse(is);
			loadBeanDefinitions(document);
		} catch (ParserConfigurationException e) {
			logger.error("ParserConfiguration exception parsing XML", e);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * load document beanDefinitios
	 * @param document
	 */
	private void loadBeanDefinitions(Document document) {
		if(null == document) {
			logger.warn("load beanDefinitions parse document is fail");
			return;
		}
		
		NodeList nl = document.getElementsByTagName("beans");
		
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			loadBeanDefinition((Element) n);
		}

		// Ask superclass to eagerly instantiate singletons
		// ListableBeanFactoryImpl realize
		preInstantiateSingletons();
	}
	
	/**
	 * parse element beanDefinition
	 * @param n
	 */
	private void loadBeanDefinition(Element element) {
		//解析beanId
		String id = element.getAttribute(ID_ATTRIBUTE);
		String beanName = element.getAttribute(NAME_ATTRIBUTE);
		AbstractBeanDefinition beanDefinition;
		
		//获取element的属性
		PropertyValues pvs = getPropertyValueSubElements(element);
		//解析element beanDefinition
		beanDefinition = parseBeanDefinition(element, id, pvs);
		//注册beanDefinition  ListableBeanFactoryImpl实现
		registerBeanDefinition(id, beanDefinition);
		
		String name = element.getAttribute(NAME_ATTRIBUTE);
		if (name != null && !"".equals(name)) {
			//注册别名 TODO
			//registerAlias(id, name);
		}
	}
	
	/**
	 * 解析Element 
	 * @param el
	 * @param beanName
	 * @param pvs
	 * @return
	 */
	private AbstractBeanDefinition parseBeanDefinition(Element el, String beanName, PropertyValues pvs) {
		String classname = null;
		boolean singleton = true;
		String parent = null;
		//解析单列属性
		if(el.hasAttribute(SINGLETON_ATTRIBUTE)) {
			singleton = TRUE_ATTRIBUTE_VALUE.equals(el.getAttribute(SINGLETON_ATTRIBUTE));
		}
		
		try {
			if(el.hasAttribute(CLASS_ATTRIBUTE)) {
				classname = el.getAttribute(CLASS_ATTRIBUTE);
			}
			if (el.hasAttribute(PARENT_ATTRIBUTE)) {
				parent = el.getAttribute(PARENT_ATTRIBUTE);
			}
			
			if (classname == null && parent == null)
				throw new RuntimeException("No classname or parent in bean definition [" + beanName + "]", null);
			//classname 标签属性  初始化并返回RootBeanDefinition
			if (classname != null) {
				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				return new RootBeanDefinition(Class.forName(classname, true, cl), pvs, singleton);
			}else {
				//子节点属性  TODO
				return new ChildBeanDefinition(parent, pvs, singleton);
			}
			
		}catch (ClassNotFoundException ex) {
			throw new RuntimeException("Error creating bean with name [" + beanName + "]: class '" + classname + "' not found", ex);
		}
	}
	
	
	/**
	 * 获取bean属性
	 * @param beanEle
	 * @return
	 */
	private PropertyValues getPropertyValueSubElements(Element beanEle) {
		NodeList nl = beanEle.getElementsByTagName(PROPERTY_ELEMENT);
		MutablePropertyValues pvs = new MutablePropertyValues();
		for (int i = 0; i < nl.getLength(); i++) {
			Element propEle = (Element) nl.item(i);
			parsePropertyElement(pvs, propEle);
		}
		return pvs;
	}
	
	private void parsePropertyElement(MutablePropertyValues pvs, Element e) throws DOMException {
		String propertyName = e.getAttribute(NAME_ATTRIBUTE);
		if (propertyName == null || "".equals(propertyName))
			logger.error("Property without a name", null);

		Object val = getPropertyValue(e);
		pvs.addPropertyValue(new PropertyValue(propertyName, val));
	}
	
	
	private Object getPropertyValue(Element e) {
		String distinguishedValue = e.getAttribute(DISTINGUISHED_VALUE_ATTRIBUTE);
		if (distinguishedValue != null && distinguishedValue.equals(NULL_DISTINGUISHED_VALUE)) {
			return null;
		}
		// Can only have one element child:
		// value, ref, collection
		NodeList nl = e.getChildNodes();
		Element childEle = null;
		for (int i = 0; i < nl.getLength(); i++) {
			if (nl.item(i) instanceof Element) {
				if (childEle != null)
					logger.error("<property> element can have only one child element, not " + nl.getLength(), null);
				childEle = (Element) nl.item(i);
			}
		}

		return parsePropertySubelement(childEle);
	}

	private Object parsePropertySubelement(Element childEle) {
		// TODO Auto-generated method stub
		return null;
	}

}
