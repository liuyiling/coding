package com.liuyiling.common.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NamespaceHandler extends NamespaceHandlerSupport {

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("mysql", new BeanDefinitionParser(MysqlClientFactoryBean.class));
	}

}
