package org.myspring.context.support;

import java.io.IOException;
import java.io.InputStream;
import org.myspring.context.MyApplicationContext;
import com.july.beans.utils.StringUtils;


/**
 * Standalone XML application context, taking the context definition
 * files from the file system or from URLs. Mainly useful for test
 * harnesses, but also for standalone environments.
 * @author hejian
 *
 */
public class FileSystemXmlApplicationContext extends AbstractXmlApplicationContext {
	
	//资源文件
	private String configLocation;
	
	public FileSystemXmlApplicationContext(String locations)
		    throws IOException {
			this(StringUtils.commaDelimitedListToStringArray(locations));
	}
	
	/**
	 * 多参构造
	 * @param locations
	 * @throws IOException
	 */
	public FileSystemXmlApplicationContext(String[] locations)
		    throws IOException {
			if (locations.length == 0) {
				throw new RuntimeException("At least 1 config location required");
			}
			this.configLocation = locations[locations.length - 1];
			logger.debug("Trying to open XML application context file '" + this.configLocation + "'");

			// Recurse
			if (locations.length > 1) {
				// There were parent(s)
				String[] parentLocations = new String[locations.length - 1];
				System.arraycopy(locations, 0, parentLocations, 0, locations.length - 1);
				if (logger.isDebugEnabled()) {
					logger.debug("Setting parent context for locations: [" +
											 StringUtils.arrayToDelimitedString(parentLocations, ","));
				}
				//MyApplicationContext parent = createParentContext(parentLocations);
				//setParent(parent);
			}
			// initialize this context
			refresh();
		}
	
	/**
	 * 创建applicationContext
	 * @param locations
	 * @return
	 * @throws IOException
	 */
	protected MyApplicationContext createParentContext(String[] locations) throws IOException {
		return new FileSystemXmlApplicationContext(locations);
	}
	
	/**
	 * 获取配件文件流
	 */
	@Override
	protected final InputStream getInputStreamForBeanFactory() throws IOException {
		return getResourceAsStream(this.configLocation);
	}
}
