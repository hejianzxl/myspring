package com.july.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.july.beans.factory.MyListableBeanFactory;
import com.july.context.aware.ApplicationContextAware;

public abstract class AbstractApplicationContext implements MyApplicationContext{

	protected final Log				logger				= LogFactory.getLog(getClass());
	public static final String		OPTIONS_BEAN_NAME	= "contextOptions";

	private MyApplicationContext	parent;

	private long					startupTime;

	private Map						sharedObjects		= new ConcurrentHashMap<String, Object>(16);

	public AbstractApplicationContext() {
	}

	public AbstractApplicationContext(MyApplicationContext parent) {
		this.parent = parent;
	}

	/**
	 * 返回父Context
	 * 
	 * @return
	 */
	public MyApplicationContext getParent() {
		return parent;
	}

	/**
	 * 设置context parent
	 * 
	 * @param ac
	 */
	protected void setParent(MyApplicationContext ac) {
		this.parent = ac;
	}

	/**
	 * 返回加载开始时间
	 * 
	 * @return
	 */
	public final long getStartupDate() {
		return startupTime;
	}

	public final void refresh() {
		//获取启动开始
		this.startupTime = System.currentTimeMillis();
		//初始化beanFactory
		refreshBeanFactory();
		if (getBeanDefinitionCount() == 0) {
			logger.warn("No beans defined in ApplicationContext: " + getClass().getName());
		}else {
			logger.info(getBeanDefinitionCount() + " beans defined in ApplicationContext: " + getClass().getName());
		}
		
		configureAllManagedObjects();
		//初始化Listeners 未实现
		//refreshListeners();
		loadOptions();
		
		onRefresh();

	}

	/**
	 * loadd options
	 */
	private void loadOptions() {
		
	}

	/**
	 * 回调方法可以添加特定于上下文的刷新覆盖
	 */
	protected void onRefresh() {
		// For subclasses: do nothing by default.
	}

	private void configureAllManagedObjects() {

	}

	public final InputStream getResourceAsStream(String location) throws IOException {
		try {
			// try URL
			URL url = new URL(location);
			logger.debug("Opening as URL: " + location);
			return url.openStream();
		} catch (MalformedURLException ex) {
			// no URL -> try (file) path
			InputStream in = getResourceByPath(location);
			if (in == null) {
				throw new FileNotFoundException(
						"Location '" + location + "' isn't a URL and cannot be interpreted as (file) path");
			}
			return in;
		}
	}

	/**
	 * 读取配置文件流
	 * 
	 * @param location
	 * @return
	 * @throws FileNotFoundException
	 */
	private InputStream getResourceByPath(String path) throws FileNotFoundException {
		if (!path.startsWith("/")) {
			// always use root,
			// as loading relative to this class' package doesn't make sense
			path = "/" + path;
		}
		return getClass().getResourceAsStream(path);
	}

	public String getResourceBasePath() {
		return (new File("")).getAbsolutePath() + File.separatorChar;
	}

	public synchronized Object sharedObject(String key) {
		return this.sharedObjects.get(key);
	}

	public synchronized void shareObject(String key, Object o) {
		logger.info("Set shared object '" + key + "'");
		this.sharedObjects.put(key, o);
	}

	public synchronized Object removeSharedObject(String key) {
		logger.info("Removing shared object '" + key + "'");
		Object o = this.sharedObjects.remove(key);
		if (o == null) {
			logger.warn("Shared object '" + key + "' not present; could not be removed");
		} else {
			logger.info("Removed shared object '" + key + "'");
		}
		return o;
	}

	protected void configureManagedObject(Object o) {
		if (o instanceof ApplicationContextAware) {
			logger.debug("Setting application context on ApplicationContextAware object [" + o + "]");
			ApplicationContextAware aca = (ApplicationContextAware) o;
			aca.setApplicationContext(this);
		}
	}

	public Object getBean(String name) {
		Object bean = getBeanFactory().getBean(name);
		configureManagedObject(bean);
		return bean;
	}

	public Object getBean(String name, Class requiredType) {
		Object bean = getBeanFactory().getBean(name, requiredType);
		configureManagedObject(bean);
		return bean;
	}

	public boolean isSingleton(String name) {
		return getBeanFactory().isSingleton(name);
	}
	
	public <T> boolean isSingleton(Class<T> classss) {
		return false;
	}

	public String[] getAliases(String name) {
		return getBeanFactory().getAliases(name);
	}

	// ---------------------------------------------------------------------
	// Implementation of ListableBeanFactory
	// ---------------------------------------------------------------------

	public int getBeanDefinitionCount() {
		return getBeanFactory().getBeanDefinitionCount();
	}

	public String[] getBeanDefinitionNames() {
		return getBeanFactory().getBeanDefinitionNames();
	}

	public String[] getBeanDefinitionNames(Class type) {
		return getBeanFactory().getBeanDefinitionNames(type);
	}

	protected abstract void refreshBeanFactory();

	protected abstract MyListableBeanFactory getBeanFactory();

}
