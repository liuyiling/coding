package com.liuyiling.common.spring;

import com.liuyiling.common.util.UniversalLogger;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.*;

/**
 * Created by liuyl on 2017/1/1.
 */
public abstract class CacheableObjectFactoryBean<T> extends AbstractFactoryBean<T> {

    private static Map<String, Object> beanCache = new HashMap<String, Object>();

    protected boolean cacheInstance = true;

    protected List<BeanProperty<T>> properties = new ArrayList<BeanProperty<T>>();

    @SuppressWarnings("unchecked")
    @Override
    protected T createInstance() throws Exception {
        String key = this.getKey().toString();
        Object bean = this.isCacheInstance() ? beanCache.get(key) : null;
        if (bean == null) {
            long startTime = System.currentTimeMillis();
            bean = this.doCreateInstance();
            beanCache.put(key, bean);
            long cost = System.currentTimeMillis() - startTime;
            UniversalLogger.info("[createInstance]" + this.getClass().getName() + " object key:" + key
                    + " init cost time:" + cost);
        } else {
            UniversalLogger.info("[createInstance]" + this.getClass().getName()
                    + " reuse bean instance by key:" + key);
        }
        return (T) bean;
    }

    public boolean isCacheInstance() {
        return cacheInstance;
    }

    public void setCacheInstance(boolean cacheInstance) {
        this.cacheInstance = cacheInstance;
    }

    protected StringBuilder getKey(){
        StringBuilder buf = new StringBuilder();
        buf.append(this.getKeyPrefix());
        Collections.sort(this.properties);
        for (BeanProperty<T> property : properties) {
            buf.append(property.getName()).append(":").append(property.getValue());
        }
        return buf;
    }

    protected abstract String getKeyPrefix();

    protected abstract Object doCreateInstance() throws Exception;

    public static Map<String, Object> getBeanCache(){
        return Collections.unmodifiableMap(beanCache);
    }

    public abstract static class BeanProperty<T> implements Comparable<BeanProperty<T>> {
        private String name;
        private Object value;

        public BeanProperty(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }

        public abstract void apply(T target);

        @Override
        public int compareTo(BeanProperty<T> o) {
            return this.name.compareTo(o.name);
        }

    }
}
