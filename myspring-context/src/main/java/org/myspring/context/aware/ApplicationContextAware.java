package org.myspring.context.aware;

import org.myspring.context.AbstractApplicationContext;

/**
 * ApplicationContextAware
 * @author hejian
 *
 */
public interface ApplicationContextAware {
	
	public void setApplicationContext(AbstractApplicationContext abstractApplicationContext);

}
