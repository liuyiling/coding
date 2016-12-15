package com.liuyiling.coding.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by liuyl on 2016/12/15.
 */
public interface BasicRedisClient {

    Logger LOGGER = LoggerFactory.getLogger("commons-redis");

    boolean expire(final String key, final int seconds);

    boolean expireAt(final String key, final long unixTime);

    Long ttl(final String key);

    boolean persist(final String key);

    <T extends Serializable> Boolean setex(final String key, final int seconds, final T value);



}
