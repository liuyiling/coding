package com.liuyiling.common.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.w3c.dom.*;

/**
 * Created by liuyl on 2017/1/1.
 */
public class BeanDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    private static final String PROPERTY_ELEMENT = "property";
    private static final String VALUE_ELEMENT = "value";
    private static final String NAME_ELEMENT = "name";

    private static final String PARENT_ATTRIBUTE = "parent";
    private static final String LAZYINIT_ATTRIBUTE = "lazy-init";

    private static final String TRUE = "true";

    private Class<? extends FactoryBean<?>> factoryBeanClass;

    public BeanDefinitionParser(Class<? extends FactoryBean<?>> factoryBeanClass) {
        this.factoryBeanClass = factoryBeanClass;
    }

    @Override
    protected Class<? extends FactoryBean<?>> getBeanClass(Element element) {
        return this.factoryBeanClass;
    }

    @Override
    protected void postProcess(BeanDefinitionBuilder builder, Element element) {
        NodeList list = element.getElementsByTagName(PROPERTY_ELEMENT);
        if (list != null && list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                Node item = list.item(i);
                String propertyName = extractPropertyName(getAttributeValue(item, NAME_ELEMENT));
                Assert.state(StringUtils.hasText(propertyName),
                        "Illegal property name returned from 'extractPropertyName(String)': cannot be null or empty.");
                String value = getAttributeValue(item, VALUE_ELEMENT);
                if (value == null) {
                    value = getTextElementValue(item, VALUE_ELEMENT);
                }
                Assert.state(value != null, "Illegal property value, cannot be null.");
                builder.addPropertyValue(propertyName, value);
            }
        }
        if (TRUE.equals(element.getAttribute(LAZYINIT_ATTRIBUTE))) {
            builder.setLazyInit(true);
        } else {
            builder.setLazyInit(false);
        }
    }

    @Override
    protected boolean isEligibleAttribute(String attributeName) {
        // 过滤掉 id 和parent,lazyInit属性
        return !ID_ATTRIBUTE.equals(attributeName) && !PARENT_ATTRIBUTE.equals(attributeName)
                && !LAZYINIT_ATTRIBUTE.equals(attributeName);
    }

    /**
     * 如果没有显式配置id，则自动生成
     */
    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    @Override
    protected String getParentName(Element element) {
        String parent = element.getAttribute(PARENT_ATTRIBUTE);
        if (!StringUtils.hasText(parent)) {
            return null;
        }
        return parent.trim();
    }

    private static String getAttributeValue(Node node, String attributeName) {
        NamedNodeMap attributes = node.getAttributes();
        for (int x = 0; x < attributes.getLength(); x++) {
            Attr attribute = (Attr) attributes.item(x);
            if (attribute.getLocalName().equals(attributeName)) {
                return attribute.getValue();
            }
        }
        return null;
    }

    private static String getTextElementValue(Node node, String elementName) {
        NodeList list = node.getChildNodes();
        for (int x = 0; x < list.getLength(); x++) {
            Node c = list.item(x);
            // fix the bug: c.getLocalName will be empty
            if (elementName.equals(c.getLocalName())) {
                return c.getTextContent();
            }
        }
        return null;
    }

}
