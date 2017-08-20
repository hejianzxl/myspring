package org.myspring.context.support;

import java.io.IOException;
import java.io.InputStream;
import org.myspring.context.AbstractApplicationContext;
import org.myspring.context.MyApplicationContext;
import com.july.beans.factory.MyListableBeanFactory;
import com.july.beans.factory.xml.XmlBeanFactory;

/**
 * 抽象AbstractApplicationContext
 * 
 * @author hejian
 *
 */
public abstract class AbstractXmlApplicationContext extends AbstractApplicationContext {
	/** Default BeanFactory for this context */
	private MyListableBeanFactory listableBeanFactory;

	public AbstractXmlApplicationContext() {
	}

	public AbstractXmlApplicationContext(MyApplicationContext parent) {
		super(parent);
	}

	public void refreshBeanFactory() {
		logger.info("spring refresh init beanFactory");
		InputStream is = null;
		try {
			is = getInputStreamForBeanFactory();
			listableBeanFactory = new XmlBeanFactory(is);
			logger.info("BeanFactory for application context is [" + listableBeanFactory + "]");
		} catch (IOException ex) {
			logger.error("IOException parsing XML document for ", ex);
		}finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException ex) {
				logger.error("IO CLOSER IS IOException");
			}
		}
	}
	
	/***
	 * 子类实现
	 * 获取输入流
	 * @return
	 * @throws IOException
	 */
	protected abstract InputStream getInputStreamForBeanFactory() throws IOException;
}
