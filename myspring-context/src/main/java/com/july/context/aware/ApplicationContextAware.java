package com.july.context.aware;

import com.july.context.AbstractApplicationContext;

/**
 * ApplicationContextAware
 * @author hejian
 *
 */
public interface ApplicationContextAware {
	
	public void setApplicationContext(AbstractApplicationContext abstractApplicationContext);

}
