package com.liuyiling.coding.redis.client;

import com.liuyiling.coding.redis.JedisPortCallback;
import com.liuyiling.coding.redis.log.RedisLog;
import com.liuyiling.coding.redis.pool.JedisPoolFactory;
import com.liuyiling.common.util.CodecHandler;
import com.liuyiling.common.util.StatLog;
import com.liuyiling.common.util.TimeStatUtil;
import com.liuyiling.common.util.UniversalLogger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对应连接到某一个端口的redisClient
 * Created by liuyl on 2016/12/20.
 */

public class RedisClient implements BasicRedisClient {

    private static final String REDIS_RET_OK = "OK";
    private static final int REDIS_SLOW_TIME = 50;

    private GenericObjectPoolConfig poolConfig;
    private JedisPool jedisPool;

    private AtomicBoolean healthy = new AtomicBoolean(true);
    private AtomicInteger backendFails = new AtomicInteger(0);
    private static final int BACKEND_FAILS_THRESHOLD = 20;

    private long recoverInterval = 30000;

    private URI uri;
    private int resource;

    private int maxTryTimes = 2;
    private boolean throwJedisException = true;

    private int tryTime = 1;

    public void setTryTime(int tryTime) {
        if (tryTime > 1) {
            this.tryTime = tryTime;
        }
    }

    public boolean isHealthy() {
        return healthy.get();
    }

    public void setServer(final String server) {
        this.uri = URI.create(server);
        TimeStatUtil.register(TimeStatUtil.REDIS_TYPE + uri.getPort());
        this.resource = TimeStatUtil.REDIS_TYPE + uri.getPort();
    }

    public void setRecoverInterval(long recoverInterval) {
        this.recoverInterval = recoverInterval;
    }

    public void setPoolConfig(final GenericObjectPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    //spring初始化时会调用该方法
    public void init() {
        this.jedisPool = JedisPoolFactory.getPool(this.poolConfig, uri);

        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (!healthy.get()) {
                            Jedis jedis = null;
                            try {
                                jedis = jedisPool.getResource();
                                jedis.ping();
                                backendFails.set(0);
                                healthy.compareAndSet(false, true);
                                UniversalLogger.info("RedisClient init comes back to life:uri=" + uri.toString());
                            } catch (Exception e) {
                            } finally {
                                if (jedis != null) {
                                    jedis.close();
                                }
                            }
                        }
                    }
                }, 0, recoverInterval);
    }

    public GenericObjectPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public boolean isThrowJedisException() {
        return throwJedisException;
    }

    public void setThrowJedisException(boolean throwJedisException) {
        this.throwJedisException = throwJedisException;
    }

    public synchronized void close() {
        if (this.jedisPool != null) {
            this.jedisPool.destroy();
        }
    }

    public void setmaxTryTime(int retry) {
        maxTryTimes = retry;
        TimeStatUtil.setMaxTryTime(retry);
    }

    public <K> K callable(JedisPortCallback<K> callback) {
        return callable(callback, maxTryTimes);
    }

    public <K> K callable(JedisPortCallback<K> callback, int tryTime) {
        return callable(callback, tryTime, false);
    }

    public <K> K callable(JedisPortCallback<K> callback, int tryTime, boolean isMulti) {
        //快速失败
        if (!healthy.get()) {
            return null;
        }

        K value = null;
        Jedis jedis = null;
        long startTime = System.currentTimeMillis();
        long costTime;
        long endTime;
        int count = 0;

        try {
            while (count++ < tryTime) {
                try {
                    jedis = jedisPool.getResource();
                    value = callback.call(jedis);
                    costTime = System.currentTimeMillis() - startTime;
                    StatLog.incProcessTime("jedis." + callback.getName(), 1, costTime);
                    if (LOGGER.isDebugEnabled() && costTime < REDIS_SLOW_TIME) {
                        LOGGER.debug(getClientSign(jedis) + " " + callback.getName()
                                + ", key: " + callback.getKey() + " result:" + value);
                    } else if (costTime >= REDIS_SLOW_TIME) {
                        StatLog.inc("jedis.slowget");
                        LOGGER.warn(getClientSign(jedis) + " " + callback.getName()
                                + ", cost " + costTime + " key: " + callback.getKey()
                                + " result:" + value);
                    }

                    RedisLog.slowLog(getClientSign(jedis), costTime);
                    break;
                } catch (JedisConnectionException jce) {
                    // should retry
                    LOGGER.error(getClientSign(jedis) + " " + callback.getName() + " fail:" + jce);
                } catch (JedisException je) {
                    LOGGER.error(getClientSign(jedis) + " " + callback.getName() + " fail:" + je);

                    if (je.getCause() instanceof SocketTimeoutException) {
                        // should retry
                        continue;
                    }

                    // mark fail
                    if (backendFails.incrementAndGet() >= BACKEND_FAILS_THRESHOLD) {
                        LOGGER.error("jedis port {} becomes unhealthy", uri.toString());
                        healthy.set(false);
                    }

                    if (throwJedisException) {
                        throw je;
                    }
                    break;
                } catch (final Exception e) {
                    LOGGER.error(getClientSign(jedis) + " " + callback.getName()
                            + " error:", e);

                    // mark fail
                    if (backendFails.incrementAndGet() >= BACKEND_FAILS_THRESHOLD) {
                        LOGGER.error("jedis port {} becomes unhealthy", uri.toString());
                        healthy.set(false);
                    }

                    break;
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
            }
        }  finally {
            int realPort = resource;
            if (isMulti) {
                realPort = resource + TimeStatUtil.MULTI_TYPE;
            }
            endTime = System.currentTimeMillis();
            //从开始到finally中结束的时间，包括异常和重试时间
            costTime = endTime - startTime;
            TimeStatUtil.addElapseTimeStat(realPort, callback.isWriter(), startTime, costTime);
        }

        // every time this method successfully returns, server should be marked healthy
        if (value != null) {
            backendFails.set(0);
            healthy.compareAndSet(false, true);
        }

        return value;
    }

    private String getClientSign(Jedis jedis) {
        try {
            if (jedis != null && jedis.getClient() != null) {
                return "REDIS (" + jedis.getClient().getHost() + ")" + uri.toString();
            } else {
                return toString();
            }
        } catch (Exception e) {
            return "REDIS " + uri.toString();
        }
    }

    @Override
    public String toString() {
        return "RedisClient{" +
                "uri=" + uri +
                ", resource=" + resource +
                '}';
    }



    @Override
    public boolean expire(final String key, final int seconds) {
        Long result = callable(new JedisPortCallback<Long>("expire", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.expire(CodecHandler.encode(key), seconds);
            }
        });
        return !(result == null || result != 1);
    }

    @Override
    public boolean expireAt(final String key, final long unixTime) {
        Long result = callable(new JedisPortCallback<Long>("expireat", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.expireAt(CodecHandler.encode(key), unixTime);
            }
        });
        return !(result == null || result != 1);
    }

    @Override
    public Long ttl(final String key) {
        return callable(new JedisPortCallback<Long>("ttl", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.ttl(CodecHandler.encode(key));
            }
        });
    }

    @Override
    public boolean persist(final String key) {
        Long result = callable(new JedisPortCallback<Long>("persist", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.persist(CodecHandler.encode(key));
            }
        });
        return !(result == null || result != 1);
    }

    @Override
    public Boolean hdel(final String key, final String field) {
        callable(new JedisPortCallback<Long>("hdel", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.hdel(CodecHandler.encode(key), CodecHandler.encode(field));
            }
        });
        return true;
    }

    @Override
    public Long incr(final String key) {
        return callable(new JedisPortCallback<Long>("incr", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.incr(CodecHandler.encode(key));
            }
        }, tryTime);
    }

    @Override
    public Long hincrBy(final String key, final String field, final long value) {
        return callable(new JedisPortCallback<Long>("hincrBy", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.hincrBy(CodecHandler.encode(key), CodecHandler.encode(field), value);
            }
        }, tryTime);
    }

    @Override
    public Long decr(final String key) {
        return callable(new JedisPortCallback<Long>("decr", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.decr(CodecHandler.encode(key));
            }
        }, tryTime);
    }

    @Override
    public Set<String> hkeys(final String key) {
        return callable(new JedisPortCallback<Set<String>>("hkeys", key, false) {
            @Override
            public Set<String> call(Jedis jedis) {
                return CodecHandler.decodeSet(jedis.hkeys(CodecHandler.encode(key)), String.class);
            }
        });
    }

    @Override
    public <T extends Serializable> List<T> hvals(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<List<T>>("hvals", key, false) {
            @Override
            public List<T> call(Jedis jedis) {
                return CodecHandler.decodeList(jedis.hvals(CodecHandler.encode(key)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Map<String, T> hgetAll(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<String, T>>("hgetAll", key, false) {
            @Override
            public Map<String, T> call(Jedis jedis) {
                Map<byte[], byte[]> temp = jedis.hgetAll(CodecHandler.encode(key));
                Map<String, T> result = new HashMap<String, T>();
                for (Map.Entry<byte[], byte[]> entry : temp.entrySet()) {
                    result.put(CodecHandler.decode(entry.getKey(), String.class), CodecHandler.decode(entry.getValue(), clazz));
                }

                return result;
            }
        });
    }

    @Override
    public Long hlen(final String key) {
        return callable(new JedisPortCallback<Long>("hlen", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.hlen(CodecHandler.encode(key));
            }
        });
    }

    @Override
    public <T extends Serializable> Boolean setex(final String key, final int seconds, final T value) {
        String result = callable(new JedisPortCallback<String>("setex", key, true) {
            @Override
            public String call(Jedis jedis) {
                return jedis.setex(CodecHandler.encode(key), seconds, CodecHandler.encode(value));
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public <T extends Serializable> Boolean setnx(final String key, final T value) {
        Long result = callable(new JedisPortCallback<Long>("setnx", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.setnx(CodecHandler.encode(key), CodecHandler.encode(value));
            }
        });

        return result == 1;
    }

    @Override
    public <T extends Serializable> Boolean set(final String key, final T value) {
        String result = callable(new JedisPortCallback<String>("set", key, true) {
            @Override
            public String call(Jedis jedis) {
                return jedis.set(CodecHandler.encode(key), CodecHandler.encode(value));
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public <T extends Serializable> T get(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<T>("get", key, false) {
            @Override
            public T call(Jedis jedis) {
                return CodecHandler.decode(jedis.get(CodecHandler.encode(key)), clazz);
            }
        });
    }


    @Override
    public Boolean exists(final String key) {
        return callable(new JedisPortCallback<Boolean>("exists", key, false) {
            @Override
            public Boolean call(Jedis jedis) {
                return jedis.exists(CodecHandler.encode(key));
            }
        });
    }

    @Override
    public Long del(final String... keys) {
        return callable(new JedisPortCallback<Long>("del", Arrays.toString(keys), true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.del(CodecHandler.encodeParams(keys));
            }
        });
    }

    @Override
    public <T extends Serializable> List<T> mget(final Class<T> clazz, final String... keys) {
        return callable(new JedisPortCallback<List<T>>("mget", Arrays.toString(keys), false) {
            @Override
            public List<T> call(Jedis jedis) {
                return CodecHandler.decodeList(jedis.mget(CodecHandler.encodeParams(keys)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Boolean mset(final Map<String, T> keyValueMap) {
        String result = callable(new JedisPortCallback<String>("mset", null, true) {
            @Override
            public String call(Jedis jedis) {
                List<byte[]> args = new ArrayList<byte[]>();
                for (Map.Entry<String, T> entry : keyValueMap.entrySet()) {
                    args.add(entry.getKey().getBytes());
                    args.add(CodecHandler.encode(entry.getValue()));
                }
                return jedis.mset(args.toArray(new byte[keyValueMap.size() * 2][]));
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public Long decrBy(final String key, final long integer) {
        return callable(new JedisPortCallback<Long>("decrBy", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.decrBy(CodecHandler.encode(key), integer);
            }
        }, tryTime);
    }

    @Override
    public Long incrBy(final String key, final long integer) {
        return callable(new JedisPortCallback<Long>("incrBy", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.incrBy(CodecHandler.encode(key), integer);
            }
        }, tryTime);
    }

    @Override
    public <T extends Serializable> Long hset(final String key, final String field, final T value) {
        return callable(new JedisPortCallback<Long>("hset", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.hset(CodecHandler.encode(key), CodecHandler.encode(field), CodecHandler.encode(value));
            }
        });
    }

    @Override
    public <T extends Serializable> T hget(final String key, final String field, final Class<T> clazz) {
        return callable(new JedisPortCallback<T>("hget", key, true) {
            @Override
            public T call(Jedis jedis) {
                return CodecHandler.decode(jedis.hget(CodecHandler.encode(key), CodecHandler.encode(field)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Boolean hmset(final String key, final Map<String, T> keyValueMap) {
        String result = callable(new JedisPortCallback<String>("hmset", null, true) {
            @Override
            public String call(Jedis jedis) {
                Map<byte[], byte[]> args = new HashMap<byte[], byte[]>();
                for (Map.Entry<String, T> entry : keyValueMap.entrySet()) {
                    args.put(entry.getKey().getBytes(), CodecHandler.encode(entry.getValue()));
                }
                return jedis.hmset(CodecHandler.encode(key), args);
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public <T extends Serializable> List<T> hmget(final Class<T> clazz, final String key, final String... fields) {
        return callable(new JedisPortCallback<List<T>>("hmget", key, false) {
            @Override
            public List<T> call(Jedis jedis) {
                return CodecHandler.decodeList(jedis.hmget(CodecHandler.encode(key), CodecHandler.encodeParams(fields)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Long rpush(final String key, final T... values) {
        return callable(new JedisPortCallback<Long>("rpush", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.rpush(CodecHandler.encode(key), CodecHandler.encodeParams(values));
            }
        });
    }

    @Override
    public <T extends Serializable> Long lpush(final String key, final T... values) {
        return callable(new JedisPortCallback<Long>("lpush", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.lpush(CodecHandler.encode(key), CodecHandler.encodeParams(values));
            }
        });
    }

    @Override
    public Long llen(final String key) {
        return callable(new JedisPortCallback<Long>("llen", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.llen(CodecHandler.encode(key));
            }
        });
    }

    @Override
    public <T extends Serializable> List<T> lrange(final String key, final int start, final int end, final Class<T> clazz) {
        return callable(new JedisPortCallback<List<T>>("lrange", key, false) {
            @Override
            public List<T> call(Jedis jedis) {
                List<byte[]> temp = jedis.lrange(CodecHandler.encode(key), start, end);
                return CodecHandler.decodeList(temp, clazz);
            }
        });
    }

    @Override
    public Boolean ltrim(final String key, final int start, final int end) {
        String result = callable(new JedisPortCallback<String>("ltrim", key, true) {
            @Override
            public String call(Jedis jedis) {
                return jedis.ltrim(CodecHandler.encode(key), start, end);
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public <T extends Serializable> T lindex(final String key, final int index, final Class<T> clazz) {
        return callable(new JedisPortCallback<T>("lindex", key, false) {
            @Override
            public T call(Jedis jedis) {
                return CodecHandler.decode(jedis.lindex(CodecHandler.encode(key), index), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Boolean lset(final String key, final int index, final T value) {
        String result = callable(new JedisPortCallback<String>("lset", key, false) {
            @Override
            public String call(Jedis jedis) {
                return jedis.lset(CodecHandler.encode(key), index, CodecHandler.encode(value));
            }
        });
        return REDIS_RET_OK.equalsIgnoreCase(result);
    }

    @Override
    public <T extends Serializable> Long lrem(final String key, final int count, final T value) {
        return callable(new JedisPortCallback<Long>("lrem", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.lrem(CodecHandler.encode(key), count, CodecHandler.encode(value));
            }
        });
    }

    @Override
    public <T extends Serializable> Long zadd(final String key, final double score, final T member) {
        return callable(new JedisPortCallback<Long>("zadd", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zadd(CodecHandler.encode(key), score, CodecHandler.encode(member));
            }
        });
    }

    @Override
    public <T extends Serializable> Long zadd(final String key, final Map<T, Double> memberScores) {
        return callable(new JedisPortCallback<Long>("zadd", key, false) {
            @Override
            public Long call(Jedis jedis) {
                Map<byte[], Double> args = new HashMap<byte[], Double>();
                for (Map.Entry<T, Double> entry : memberScores.entrySet()) {
                    args.put(CodecHandler.encode(entry.getKey()), entry.getValue());
                }
                return jedis.zadd(CodecHandler.encode(key), args);
            }
        });
    }

    @Override
    public <T extends Serializable> Long zrem(final String key, final T member) {
        return callable(new JedisPortCallback<Long>("zrem", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zrem(CodecHandler.encode(key), CodecHandler.encode(member));
            }
        });
    }

    @Override
    public Long zremrangeByRank(final String key, final int start, final int end) {
        return callable(new JedisPortCallback<Long>("zremrangeByRank", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zremrangeByRank(CodecHandler.encode(key), start, end);
            }
        });
    }

    @Override
    public Long zremrangeByScore(final String key, final double start, final double end) {
        return callable(new JedisPortCallback<Long>("zremrangeByScore", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zremrangeByScore(CodecHandler.encode(key), start, end);
            }
        });
    }

    @Override
    public <T extends Serializable> Double zincrby(final String key, final double score, final T member) {
        return callable(new JedisPortCallback<Double>("zincrby", key, true) {
            @Override
            public Double call(Jedis jedis) {
                return jedis.zincrby(CodecHandler.encode(key), score, CodecHandler.encode(member));
            }
        }, tryTime);
    }

    @Override
    public <T extends Serializable> Set<T> zrange(final String key, final int start, final int end, final Class<T> clazz) {
        return callable(new JedisPortCallback<Set<T>>("zrange", key, false) {
            @Override
            public Set<T> call(Jedis jedis) {
                Set<T> result = new LinkedHashSet<T>();
                for (byte[] bytes : jedis.zrange(CodecHandler.encode(key), start, end)) {
                    result.add(CodecHandler.decode(bytes, clazz));
                }
                return result;
            }
        });
    }

    @Override
    public <T extends Serializable> Long zrank(final String key, final T member) {
        return callable(new JedisPortCallback<Long>("zrank", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zrank(CodecHandler.encode(key), CodecHandler.encode(member));
            }
        });
    }

    @Override
    public <T extends Serializable> Long zrevrank(final String key, final T member) {
        return callable(new JedisPortCallback<Long>("zrevrank", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zrevrank(CodecHandler.encode(key), CodecHandler.encode(member));
            }
        });
    }

    @Override
    public <T extends Serializable> Set<T> zrevrange(final String key, final int start, final int end, final Class<T> clazz) {
        return callable(new JedisPortCallback<Set<T>>("zrevrange", key, false) {
            @Override
            public Set<T> call(Jedis jedis) {
                Set<T> result = new LinkedHashSet<T>();
                for (byte[] bytes : jedis.zrevrange(CodecHandler.encode(key), start, end)) {
                    result.add(CodecHandler.decode(bytes, clazz));
                }
                return result;
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeWithScores(final String key, final int start, final int end, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrangeWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrangeWithScores(CodecHandler.encode(key), start, end), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeWithScores(final String key, final int start, final int end, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrevrangeWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrevrangeWithScores(CodecHandler.encode(key), start, end), clazz);
            }
        });
    }

    @Override
    public Long zcard(final String key) {
        return callable(new JedisPortCallback<Long>("zcard", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zcard(CodecHandler.encode(key));
            }
        });
    }

    @Override
    public <T extends Serializable> Double zscore(final String key, final T member) {
        return callable(new JedisPortCallback<Double>("zscore", key, false) {
            @Override
            public Double call(Jedis jedis) {
                return jedis.zscore(CodecHandler.encode(key), CodecHandler.encode(member));
            }
        });
    }

    @Override
    public Long zcount(final String key, final double min, final double max) {
        return callable(new JedisPortCallback<Long>("zcount", key, false) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.zcount(CodecHandler.encode(key), min, max);
            }
        });
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScore(final String key, final double min, final double max, final Class<T> clazz) {
        return callable(new JedisPortCallback<Set<T>>("zrangeByScore", key, false) {
            @Override
            public Set<T> call(Jedis jedis) {
                Set<T> result = new LinkedHashSet<T>();
                for (byte[] bytes : jedis.zrangeByScore(CodecHandler.encode(key), min, max)) {
                    result.add(CodecHandler.decode(bytes, clazz));
                }
                return result;
            }
        });
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScore(final String key, final double min, final double max, final int offset,
                                                         final int count, final Class<T> clazz) {
        return callable(new JedisPortCallback<Set<T>>("zrangeByScore", key, false) {
            @Override
            public Set<T> call(Jedis jedis) {
                Set<T> result = new LinkedHashSet<T>();
                for (byte[] bytes : jedis.zrangeByScore(CodecHandler.encode(key), min, max, offset, count)) {
                    result.add(CodecHandler.decode(bytes, clazz));
                }
                return result;
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(final String key, final double min, final double max, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrangeByScoreWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrangeByScoreWithScores(CodecHandler.encode(key), min, max), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(final String key, final double min, final double max,
                                                                           final int offset, final int count, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrangeByScoreWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrangeByScoreWithScores(CodecHandler.encode(key), min, max, offset, count), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrevrangeByScoreWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrevrangeByScoreWithScores(CodecHandler.encode(key), max, min, offset, count), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(final String key, final double max, final double min, final Class<T> clazz) {
        return callable(new JedisPortCallback<Map<T, Double>>("zrevrangeByScoreWithScores", key, false) {
            @Override
            public Map<T, Double> call(Jedis jedis) {
                return CodecHandler.decodeTuples(jedis.zrevrangeByScoreWithScores(CodecHandler.encode(key), max, min), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> T exec(JedisPortCallback<T> callback) {
        return callable(callback);
    }

    @Override
    public <T extends Serializable> T lpop(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<T>("lpop", key, true) {
            @Override
            public T call(Jedis jedis) {
                return CodecHandler.decode(jedis.lpop(CodecHandler.encode(key)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> Long linsert(final String key, final BinaryClient.LIST_POSITION where, final T pivot, final T value) {
        return callable(new JedisPortCallback<Long>("linsert", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.linsert(CodecHandler.encode(key), where, CodecHandler.encode(pivot), CodecHandler.encode(value));
            }
        });
    }

    @Override
    public Boolean rename(final String oldkey, final String newkey) {
        return callable(new JedisPortCallback<Boolean>("rename", oldkey, true) {
            @Override
            public Boolean call(Jedis jedis) {
                String result = jedis.rename(CodecHandler.encode(oldkey), CodecHandler.encode(newkey));

                return REDIS_RET_OK.equalsIgnoreCase(result);
            }
        });
    }

    @Override
    public <T extends Serializable> Boolean sismember(final String key, final T member) {
        return callable(new JedisPortCallback<Boolean>("sismember", key, false) {
            @Override
            public Boolean call(Jedis jedis) {
                return jedis.sismember(CodecHandler.encode(key), CodecHandler.encode(member));
            }
        });
    }

    @Override
    public <T extends Serializable> T spop(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<T>("spop", key, false) {
            @Override
            public T call(Jedis jedis) {
                return CodecHandler.decode(jedis.spop(CodecHandler.encode(key)), clazz);
            }
        });
    }

    @Override
    public <T extends Serializable> ScanResult<T> sscan(final String key, final String cursor, final Class<T> clazz) {
        return callable(new JedisPortCallback<ScanResult<T>>("sscan", key, false) {
            @Override
            public ScanResult<T> call(Jedis jedis) {
                ScanResult<byte[]> temp = jedis.sscan(CodecHandler.encode(key), CodecHandler.encode(cursor));
                return new ScanResult<T>(temp.getStringCursor(), CodecHandler.decodeList(temp.getResult(), clazz));
            }
        });
    }

    @Override
    public <T extends Serializable> ScanResult<T> sscan(final String key, final String cursor, final ScanParams params, final Class<T> clazz) {
        return callable(new JedisPortCallback<ScanResult<T>>("sscan", key, false) {
            @Override
            public ScanResult<T> call(Jedis jedis) {
                ScanResult<byte[]> temp = jedis.sscan(CodecHandler.encode(key), CodecHandler.encode(cursor), params);
                return new ScanResult<T>(temp.getStringCursor(), CodecHandler.decodeList(temp.getResult(), clazz));
            }
        });
    }

    @Override
    public ScanResult<String> scan(final String cursor) {
        return callable(new JedisPortCallback<ScanResult<String>>("scan", "", false) {
            @Override
            public ScanResult<String> call(Jedis jedis) {
                return jedis.scan(cursor);
            }
        });
    }

    @Override
    public ScanResult<String> scan(final String cursor, final ScanParams params) {
        return callable(new JedisPortCallback<ScanResult<String>>("scan", "", false) {
            @Override
            public ScanResult<String> call(Jedis jedis) {
                return jedis.scan(cursor, params);
            }
        });
    }

    @Override
    public <T extends Serializable> Long sadd(final String key, final T... members) {
        return callable(new JedisPortCallback<Long>("sadd", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.sadd(CodecHandler.encode(key), CodecHandler.encodeParams(members));
            }
        });
    }

    @Override
    public <T extends Serializable> Set<T> smembers(final String key, final Class<T> clazz) {
        return callable(new JedisPortCallback<Set<T>>("smembers", key, false) {
            @Override
            public Set<T> call(Jedis jedis) {
                Set<T> result = new LinkedHashSet<T>();
                for (byte[] bytes : jedis.smembers(CodecHandler.encode(key))) {
                    result.add(CodecHandler.decode(bytes, clazz));
                }
                return result;
            }
        });
    }

    @Override
    public <T extends Serializable> Long srem(final String key, final T... members) {
        return callable(new JedisPortCallback<Long>("srem", key, true) {
            @Override
            public Long call(Jedis jedis) {
                return jedis.srem(CodecHandler.encode(key), CodecHandler.encodeParams(members));
            }
        });
    }

    @Override
    public Set<String> keys(final String pattern) {
        return callable(new JedisPortCallback<Set<String>>("keys", null, false) {
            @Override
            public Set<String> call(Jedis jedis) {
                return jedis.keys(pattern);
            }
        });
    }

    @Override
    public <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(final String key, final String cursor, final Class<T> clazz) {
        return callable(new JedisPortCallback<ScanResult<Map.Entry<String, T>>>("hscan", key, false) {
            @Override
            public ScanResult<Map.Entry<String, T>> call(Jedis jedis) {
                ScanResult<Map.Entry<byte[], byte[]>> temp = jedis.hscan(CodecHandler.encode(key), CodecHandler.encode(cursor));
                List<Map.Entry<String, T>> result = new ArrayList<Map.Entry<String, T>>();
                for (Map.Entry<byte[], byte[]> entry : temp.getResult()) {
                    result.add(new AbstractMap.SimpleEntry<String, T>(CodecHandler.decode(entry.getKey(), String.class), CodecHandler.decode(entry.getValue(), clazz)));
                }
                return new ScanResult<Map.Entry<String, T>>(temp.getCursorAsBytes(), result);
            }
        });
    }

    @Override
    public <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(final String key, final String cursor, final ScanParams params, final Class<T> clazz) {
        return callable(new JedisPortCallback<ScanResult<Map.Entry<String, T>>>("hscan", key, false) {
            @Override
            public ScanResult<Map.Entry<String, T>> call(Jedis jedis) {
                ScanResult<Map.Entry<byte[], byte[]>> temp = jedis.hscan(CodecHandler.encode(key), CodecHandler.encode(cursor), params);
                List<Map.Entry<String, T>> result = new ArrayList<Map.Entry<String, T>>();
                for (Map.Entry<byte[], byte[]> entry : temp.getResult()) {
                    result.add(new AbstractMap.SimpleEntry<String, T>(CodecHandler.decode(entry.getKey(), String.class), CodecHandler.decode(entry.getValue(), clazz)));
                }
                return new ScanResult<Map.Entry<String, T>>(temp.getCursorAsBytes(), result);
            }
        });
    }
}

