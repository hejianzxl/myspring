package com.july.beans.factory;

public interface InitializingBean {
	void afterPropertiesSet() throws Exception;
}
