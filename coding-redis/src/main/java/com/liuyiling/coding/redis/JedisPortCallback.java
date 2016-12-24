package com.liuyiling.coding.redis;

import redis.clients.jedis.Jedis;

/**
 * 由于采用异步操作,故对所有的redis操作都放在统一的Callback中
 * Created by liuyl on 2016/12/20.
 */
public abstract class JedisPortCallback<V> {

    private String name;
    private String key;
    private boolean isWriter;

    public JedisPortCallback(String name) {
        super();
        this.name = name;
    }

    public JedisPortCallback(String name, String key, boolean isWriter) {
        super();
        this.name = name;
        this.key = key;
        this.isWriter = isWriter;
    }

    public abstract V call(Jedis jedis);

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setWriter(boolean isWriter) {
        this.isWriter = isWriter;
    }
}
