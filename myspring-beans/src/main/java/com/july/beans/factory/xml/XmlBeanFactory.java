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
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import com.july.beans.MutablePropertyValues;
import com.july.beans.MyBeanFactory;
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
	
	private static final String VALUE_ATTRIBUTE = "value";
	
	private static final String REF_ATTRIBUTE = "ref";

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
	
	public XmlBeanFactory(String filename, MyBeanFactory parentBeanFactory) throws Exception {
		super(parentBeanFactory);
		try {
			loadBeanDefinitions(getResourceByPath(filename));
		}
		catch (IOException ex) {
			ex.printStackTrace();
			throw new RuntimeException("Can't open file [" + filename + "]", ex);
		}
	}
	
	public XmlBeanFactory(String filename) throws Exception {
		this(filename, null);
	}
	
	public XmlBeanFactory(InputStream is) {
		loadBeanDefinitions(is);
	}

	private void loadBeanDefinitions(InputStream is) {
		logger.info("Loading XmlBeanFactory from InputStream [" + is + "]");
		//创建document解析工厂
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);
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
		instantiateSupplementaryBean();
		logger.debug("Loading bean definitions");
		NodeList nl = document.getElementsByTagName(BEAN_ELEMENT);
		
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
		//获取element的属性
		PropertyValues pvs = getPropertyValueSubElements(element);
		//解析element beanDefinition
		AbstractBeanDefinition beanDefinition = parseBeanDefinition(element, id, pvs);
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
				//子节点属性
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
		//获取所有property标签
		NodeList nl = beanEle.getElementsByTagName(PROPERTY_ELEMENT);
		MutablePropertyValues pvs = new MutablePropertyValues();
		for (int i = 0; i < nl.getLength(); i++) {
			//返回property节点
			Element propEle = (Element) nl.item(i);
			//解析property
			parsePropertyElement(pvs, propEle);
		}
		return pvs;
	}
	
	private void parsePropertyElement(MutablePropertyValues pvs, Element e) throws DOMException {
		String propertyName = e.getAttribute(NAME_ATTRIBUTE);
		if (propertyName == null || "".equals(propertyName))
			logger.error("Property without a name", null);
		
		if(StringUtils.isNotEmpty(propertyName)) {
			//解析value 
			String propertyValue = e.getAttribute(VALUE_ATTRIBUTE);
			if(StringUtils.isNotEmpty(propertyValue)) 
			pvs.addPropertyValue(new PropertyValue(propertyName, propertyValue));
		}
		//解析ref
		String propertyRef = e.getAttribute(REF_ATTRIBUTE);
		if(StringUtils.isNotEmpty(propertyRef)) 
		pvs.addPropertyValue(new PropertyValue(REF_ATTRIBUTE, propertyRef));
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
		if(null == childEle) {
			return null;
		}
		return parsePropertySubelement(childEle);
	}

	private Object parsePropertySubelement(Element ele) {
		if (ele.getTagName().equals(REF_ELEMENT)) {
			// a reference to another bean in this factory?
			String beanName = ele.getAttribute(BEAN_REF_ATTRIBUTE);
			if ("".equals(beanName)) {
				// a reference to an external bean (in a parent factory)?
				beanName = ele.getAttribute(EXTERNAL_REF_ATTRIBUTE);
				if ("".equals(beanName)) {
					throw new RuntimeException("Either 'bean' or 'external' is required for a reference");
				}
			}
			return new RuntimeException(beanName);
		}
		else if (ele.getTagName().equals(VALUE_ELEMENT)) {
			// It's a literal value
			return getTextValue(ele);
		}
		else if (ele.getTagName().equals(LIST_ELEMENT)) {
			//return getList(ele);
		}
		else if (ele.getTagName().equals(MAP_ELEMENT)) {
			//return getMap(ele);
		}
		else if (ele.getTagName().equals(PROPS_ELEMENT)) {
			//return getProps(ele);
		}
		throw new RuntimeException("Unknown subelement of <property>: <" + ele.getTagName() + ">", null);
	}
	
	
	protected InputStream getResourceByPath(String path) throws IOException {
		if (!path.startsWith("/")) {
			// always use root,
			// as loading relative to this class' package doesn't make sense
			path = "/" + path;
		}
		return getClass().getResourceAsStream(path);
	}
	
	
	private String getTextValue(Element e) {
		NodeList nl = e.getChildNodes();
		if (nl.item(0) == null) {
			// treat empty value as empty String
			return "";
		}
		if (nl.getLength() != 1 || !(nl.item(0) instanceof Text)) {
			throw new RuntimeException("Unexpected element or type mismatch: " +
			                             "expected single node of " + nl.item(0).getClass() + " to be of type Text: "
			                             + "found " + e, null);
		}
		Text t = (Text) nl.item(0);
		// This will be a String
		return t.getData();
	}
}
