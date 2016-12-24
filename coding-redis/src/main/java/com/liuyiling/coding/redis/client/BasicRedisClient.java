package com.liuyiling.coding.redis.client;

import com.liuyiling.coding.redis.JedisPortCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The basic interface which this middleware can support
 * The commands according to the order of http://doc.redisfans.com/
 * Created by liuyl on 2016/12/15.
 */
public interface BasicRedisClient {

    Logger LOGGER = LoggerFactory.getLogger("coding-redis");

    /**
     * ------------------------------------ operation for keys --------------------------------
     */
    Long del(final String... keys);

    Boolean exists(String key);

    boolean expire(final String key, final int seconds);

    boolean expireAt(final String key, final long unixTime);

    boolean persist(final String key);

    Long ttl(final String key);


    /**
     * ------------------------------------ operation for simple K-V data structure --------------------------------
     */
    Long decr(final String key);

    Long decrBy(final String key, final long integer);

    <T extends Serializable> T get(final String key, Class<T> clazz);

    Long incr(final String key);

    Long incrBy(final String key, final long integer);

    <T extends Serializable> List<T> mget(Class<T> clazz, final String... keys);

    <T extends Serializable> Boolean mset(final Map<String, T> keyValueMap);

    <T extends Serializable> Boolean set(final String key, final T value);

    <T extends Serializable> Boolean setex(final String key, final int seconds, final T value);

    <T extends Serializable> Boolean setnx(final String key, final T value);


    /**
     * ------------------------------------ operation for simple Hash data structure --------------------------------
     */
    Boolean hdel(final String key, final String field);

    <T extends Serializable> T hget(final String key, final String field, Class<T> clazz);

    <T extends Serializable> Map<String, T> hgetAll(final String key, Class<T> clazz);

    Long hincrBy(final String key, final String field, final long value);

    Set<String> hkeys(final String key);

    Long hlen(final String key);

    <T extends Serializable> List<T> hmget(Class<T> clazz, final String key, final String... fields);

    <T extends Serializable> Boolean hmset(final String key, final Map<String, T> keyValueMap);

    <T extends Serializable> Long hset(final String key, final String field, final T value);

    <T extends Serializable> List<T> hvals(final String key, Class<T> clazz);

    <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(final String key, final String cursor, Class<T> clazz);

    <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(final String key, final String cursor, final ScanParams params, Class<T> clazz);


    /**
     * ------------------------------------ operation for simple List data structure --------------------------------
     */
    <T extends Serializable> T lindex(final String key, final int index, Class<T> clazz);

    <T extends Serializable> Long linsert(final String key, final BinaryClient.LIST_POSITION where, final T pivot, final T value);

    Long llen(final String key);

    <T extends Serializable> T lpop(final String key, Class<T> clazz);

    <T extends Serializable> Long lpush(final String key, final T... value);

    <T extends Serializable> Long rpush(final String key, final T... value);

    <T extends Serializable> List<T> lrange(final String key, final int start, final int end, Class<T> clazz);

    <T extends Serializable> Long lrem(final String key, final int count, final T value);

    <T extends Serializable> Boolean lset(final String key, final int index, final T value);

    Boolean ltrim(final String key, final int start, final int end);


    /**
     * ------------------------------------ operation for simple Set data structure --------------------------------
     */
    <T extends Serializable> Long sadd(final String key, final T... member);

    <T extends Serializable> Boolean sismember(final String key, final T member);

    <T extends Serializable> Set<T> smembers(final String key, Class<T> clazz);

    <T extends Serializable> T spop(final String key, Class<T> clazz);

    <T extends Serializable> Long srem(final String key, final T... members);

    <T extends Serializable> ScanResult<T> sscan(final String key, final String cursor, Class<T> clazz);

    <T extends Serializable> ScanResult<T> sscan(final String key, final String cursor, ScanParams params, Class<T> clazz);


    /**
     * ------------------------------------ operation for simple Sorted-set(zset) data structure --------------------------------
     */
    <T extends Serializable> Long zadd(String key, double score, T member);

    <T extends Serializable> Long zadd(String key, final Map<T, Double> memberScores);

    <T extends Serializable> Long zrem(String key, T member);

    Long zremrangeByRank(String key, int start, int end);

    Long zremrangeByScore(String key, double start, double end);

    <T extends Serializable> Double zincrby(String key, double score, T member);

    <T extends Serializable> Long zrank(String key, T member);

    <T extends Serializable> Map<T, Double> zrangeWithScores(String key, int start, int end, Class<T> clazz);

    <T extends Serializable> Set<T> zrange(String key, int start, int end, Class<T> clazz);

    <T extends Serializable> Set<T> zrangeByScore(String key, double min, double max, Class<T> clazz);

    <T extends Serializable> Set<T> zrangeByScore(String key, double min, double max, int offset, int count, Class<T> clazz);

    <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(String key, double min, double max, Class<T> clazz);

    <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(String key, double min, double max, int offset, int count, Class<T> clazz);

    <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count, Class<T> clazz);

    <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(String key, double max, double min, Class<T> clazz);

    <T extends Serializable> Long zrevrank(String key, T member);

    <T extends Serializable> Set<T> zrevrange(String key, int start, int end, Class<T> clazz);

    <T extends Serializable> Map<T, Double> zrevrangeWithScores(String key, int start, int end, Class<T> clazz);

    Long zcard(String key);

    Long zcount(String key, double min, double max);

    <T extends Serializable> Double zscore(String key, T member);


    /**
     * ------------------------------------ 完全暴露jedisClient的即可欧 --------------------------------
     */
    <T extends Serializable> T exec(JedisPortCallback<T> callback);

    Set<String> keys(final String pattern);

    Boolean rename(final String oldKey, final String newKey);

    ScanResult<String> scan(final String cursor);

    ScanResult<String> scan(final String cursor, ScanParams params);

}
