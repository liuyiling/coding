package com.liuyiling.coding.redis.cluster;

import com.liuyiling.coding.redis.JedisPortCallback;
import com.liuyiling.coding.redis.client.BasicRedisClient;
import com.liuyiling.coding.redis.RedisMSServer;
import com.liuyiling.coding.redis.util.HashUtil;
import com.liuyiling.common.util.UniversalLogger;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 某个服务存储数据时需要使用的集群
 * Created by liuyl on 2016/12/20.
 */
public class RedisCluster implements BasicRedisClient {

    public static final int CONSIST_NUM = 1024;
    private List<? extends RedisMSServer> serverLists;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public void destory() {
        executorService.shutdown();

        boolean shutDownSuccess = false;
        while(!shutDownSuccess){
            try{
                shutDownSuccess = executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e){
                UniversalLogger.error("redis cluster shutdown interrupted", e);
            }
        }
    }

    public List<? extends RedisMSServer> getServers(){
        return serverLists;
    }

    public void setServerLists(List<? extends  RedisMSServer> serverLists){
        this.serverLists = serverLists;
        int step = CONSIST_NUM / serverLists.size();
        for(int i = 0; i < serverLists.size(); i++){
            serverLists.get(i).setHashMin(step * i);
            if(i == serverLists.size() - 1){
                serverLists.get(i).setHashMax(CONSIST_NUM);
            } else {
                serverLists.get(i).setHashMin(step * (i + 1));
            }
        }
    }

    private int getHashNum(String key) {
        return (int) (HashUtil.getCrc32Hash(key) % CONSIST_NUM);
    }

    @Override
    public Long del(String... keys) {
        if (keys.length == 1) {
            return getServer(keys[0]).getMaster().del(keys);
        }

        Map<Integer, List<String>> hashMap = getHashMap(keys);

        final AtomicLong result = new AtomicLong();
        final CountDownLatch latch = new CountDownLatch(hashMap.size());

        for (final Map.Entry<Integer, List<String>> entry : hashMap.entrySet()) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    result.addAndGet(serverLists.get(entry.getKey()).getMaster().del(entry.getValue().toArray(new String[entry.getValue().size()])));
                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        return result.get();
    }

    @Override
    public Boolean exists(String key) {
        return getServer(key).chooseNodeForRead().exists(key);
    }

    @Override
    public boolean expire(String key, int seconds) {
        return getServer(key).getMaster().expire(key, seconds);
    }

    @Override
    public boolean expireAt(String key, long unixTime) {
        return getServer(key).getMaster().expireAt(key, unixTime);
    }

    @Override
    public boolean persist(String key) {
        return getServer(key).getMaster().persist(key);
    }

    @Override
    public Long ttl(String key) {
        return getServer(key).chooseNodeForRead().ttl(key);
    }

    @Override
    public Long decr(String key) {
        return getServer(key).getMaster().decr(key);
    }

    @Override
    public Long decrBy(String key, long integer) {
        return getServer(key).getMaster().decrBy(key, integer);
    }

    @Override
    public <T extends Serializable> T get(String key, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().get(key, clazz);
    }

    @Override
    public Long incr(String key) {
        return getServer(key).getMaster().incr(key);
    }

    @Override
    public Long incrBy(String key, long integer) {
        return getServer(key).getMaster().incrBy(key, integer);
    }

    @Override
    public <T extends Serializable> List<T> mget(Class<T> clazz, String... keys) {
        if (keys.length == 1) {
            return getServer(keys[0]).chooseNodeForRead().mget(clazz, keys);
        }

        Map<Integer, List<String>> hashMap = getHashMap(keys);

        final Map<String, T> map = new ConcurrentHashMap<String, T>();

        final CountDownLatch latch = new CountDownLatch(hashMap.size());

        for (final Map.Entry<Integer, List<String>> entry : hashMap.entrySet()) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String[] nodeKeys = entry.getValue().toArray(new String[entry.getValue().size()]);

                    List<T> nodeResult = serverLists.get(entry.getKey()).chooseNodeForRead().mget(clazz, nodeKeys);

                    for (int i = 0; i < nodeResult.size(); i++) {
                        map.put(nodeKeys[i], nodeResult.get(i));
                    }

                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        List<T> result = new ArrayList<T>();

        for (String key : keys) {
            result.add(map.get(key));
        }

        return result;
    }

    @Override
    public <T extends Serializable> Boolean mset(Map<String, T> keyValueMap) {
        Map<Integer, List<String>> hashMap = getHashMap(keyValueMap.keySet().toArray(new String[keyValueMap.size()]));

        final CountDownLatch latch = new CountDownLatch(hashMap.size());

        for (final Map.Entry<Integer, List<String>> entry : hashMap.entrySet()) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Map<String, T> map = new HashMap<String, T>();
                    for (String key : entry.getValue()) {
                        map.put(key, keyValueMap.get(key));
                    }
                    serverLists.get(entry.getKey()).getMaster().mset(map);

                    latch.countDown();
                }
            });
        }

        try {
            latch.await();
        } catch (InterruptedException ignored) {
        }

        return true;
    }

    @Override
    public <T extends Serializable> Boolean set(String key, T value) {
        return getServer(key).getMaster().set(key, value);
    }

    @Override
    public <T extends Serializable> Boolean setex(String key, int seconds, T value) {
        return getServer(key).getMaster().setex(key, seconds, value);
    }

    @Override
    public <T extends Serializable> Boolean setnx(String key, T value) {
        return getServer(key).getMaster().setnx(key, value);
    }

    @Override
    public Boolean hdel(String key, String field) {
        return getServer(key).getMaster().hdel(key, field);
    }

    @Override
    public <T extends Serializable> T hget(String key, String field, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().hget(key, field, clazz);
    }

    @Override
    public <T extends Serializable> Map<String, T> hgetAll(String key, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().hgetAll(key, clazz);
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        return getServer(key).getMaster().hincrBy(key, field, value);
    }

    @Override
    public Set<String> hkeys(String key) {
        return getServer(key).chooseNodeForRead().hkeys(key);
    }

    @Override
    public Long hlen(String key) {
        return getServer(key).chooseNodeForRead().hlen(key);
    }

    @Override
    public <T extends Serializable> List<T> hmget(Class<T> clazz, String key, String... fields) {
        return getServer(key).chooseNodeForRead().hmget(clazz, key, fields);
    }

    @Override
    public <T extends Serializable> Boolean hmset(String key, Map<String, T> keyValueMap) {
        return getServer(key).getMaster().hmset(key, keyValueMap);
    }

    @Override
    public <T extends Serializable> Long hset(String key, String field, T value) {
        return getServer(key).getMaster().hset(key, field, value);
    }

    @Override
    public <T extends Serializable> List<T> hvals(String key, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().hvals(key, clazz);
    }

    @Override
    public <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(String key, String cursor, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().hscan(key, cursor, clazz);
    }

    @Override
    public <T extends Serializable> ScanResult<Map.Entry<String, T>> hscan(String key, String cursor, ScanParams params, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().hscan(key, cursor, params, clazz);
    }

    @Override
    public <T extends Serializable> T lindex(String key, int index, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().lindex(key, index, clazz);
    }

    @Override
    public <T extends Serializable> Long linsert(String key, BinaryClient.LIST_POSITION where, T pivot, T value) {
        return getServer(key).getMaster().linsert(key, where, pivot, value);
    }

    @Override
    public Long llen(String key) {
        return getServer(key).chooseNodeForRead().llen(key);
    }

    @Override
    public <T extends Serializable> T lpop(String key, Class<T> clazz) {
        return getServer(key).getMaster().lpop(key, clazz);
    }

    @Override
    public <T extends Serializable> Long lpush(String key, T... value) {
        return getServer(key).getMaster().lpush(key, value);
    }

    @Override
    public <T extends Serializable> Long rpush(String key, T... value) {
        return getServer(key).getMaster().rpush(key, value);
    }

    @Override
    public <T extends Serializable> List<T> lrange(String key, int start, int end, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().lrange(key, start, end, clazz);
    }

    @Override
    public <T extends Serializable> Long lrem(String key, int count, T value) {
        return getServer(key).getMaster().lrem(key, count, value);
    }

    @Override
    public <T extends Serializable> Boolean lset(String key, int index, T value) {
        return getServer(key).getMaster().lset(key, index, value);
    }

    @Override
    public Boolean ltrim(String key, int start, int end) {
        return getServer(key).getMaster().ltrim(key, start, end);
    }

    @Override
    public <T extends Serializable> Long sadd(String key, T... member) {
        return getServer(key).getMaster().sadd(key, member);
    }

    @Override
    public <T extends Serializable> Boolean sismember(String key, T member) {
        return getServer(key).chooseNodeForRead().sismember(key, member);
    }

    @Override
    public <T extends Serializable> Set<T> smembers(String key, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().smembers(key, clazz);
    }

    @Override
    public <T extends Serializable> T spop(String key, Class<T> clazz) {
        return getServer(key).getMaster().spop(key, clazz);
    }

    @Override
    public <T extends Serializable> Long srem(String key, T... members) {
        return getServer(key).getMaster().srem(key, members);
    }

    @Override
    public <T extends Serializable> ScanResult<T> sscan(String key, String cursor, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().sscan(key, cursor, clazz);
    }

    @Override
    public <T extends Serializable> ScanResult<T> sscan(String key, String cursor, ScanParams params, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().sscan(key, cursor, params, clazz);
    }

    @Override
    public <T extends Serializable> Long zadd(String key, double score, T member) {
        return getServer(key).getMaster().zadd(key, score, member);
    }

    @Override
    public <T extends Serializable> Long zadd(String key, Map<T, Double> memberScores) {
        return getServer(key).getMaster().zadd(key, memberScores);
    }

    @Override
    public <T extends Serializable> Long zrem(String key, T member) {
        return getServer(key).getMaster().zrem(key, member);
    }

    @Override
    public Long zremrangeByRank(String key, int start, int end) {
        return getServer(key).getMaster().zremrangeByRank(key, start, end);
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        return getServer(key).getMaster().zremrangeByScore(key, start, end);
    }

    @Override
    public <T extends Serializable> Double zincrby(String key, double score, T member) {
        return getServer(key).getMaster().zincrby(key, score, member);
    }

    @Override
    public <T extends Serializable> Long zrank(String key, T member) {
        return getServer(key).chooseNodeForRead().zrank(key, member);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeWithScores(String key, int start, int end, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrangeWithScores(key, start, end, clazz);
    }

    @Override
    public <T extends Serializable> Set<T> zrange(String key, int start, int end, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrange(key, start, end, clazz);
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScore(String key, double min, double max, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrangeByScore(key, min, max, clazz);
    }

    @Override
    public <T extends Serializable> Set<T> zrangeByScore(String key, double min, double max, int offset, int count, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrangeByScore(key, min, max, offset, count, clazz);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(String key, double min, double max, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrangeByScoreWithScores(key, min, max, clazz);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrangeByScoreWithScores(String key, double min, double max, int offset, int count, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrangeByScoreWithScores(key, min, max, offset, count, clazz);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrevrangeByScoreWithScores(key, max, min, offset, count, clazz);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeByScoreWithScores(String key, double max, double min, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrevrangeByScoreWithScores(key, max, min, clazz);
    }

    @Override
    public <T extends Serializable> Long zrevrank(String key, T member) {
        return getServer(key).chooseNodeForRead().zrevrank(key, member);
    }

    @Override
    public <T extends Serializable> Set<T> zrevrange(String key, int start, int end, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrevrange(key, start, end, clazz);
    }

    @Override
    public <T extends Serializable> Map<T, Double> zrevrangeWithScores(String key, int start, int end, Class<T> clazz) {
        return getServer(key).chooseNodeForRead().zrevrangeWithScores(key, start, end, clazz);
    }

    @Override
    public Long zcard(String key) {
        return getServer(key).chooseNodeForRead().zcard(key);
    }

    @Override
    public Long zcount(String key, double min, double max) {
        return getServer(key).chooseNodeForRead().zcount(key, min, max);
    }

    @Override
    public <T extends Serializable> Double zscore(String key, T member) {
        return getServer(key).chooseNodeForRead().zscore(key, member);
    }

    @Override
    public <T extends Serializable> T exec(JedisPortCallback<T> callback) {
        return null;
    }

    @Override
    public Set<String> keys(String pattern) {
        return null;
    }

    @Override
    public Boolean rename(String oldKey, String newKey) {
        return null;
    }

    @Override
    public ScanResult<String> scan(String cursor) {
        return null;
    }

    @Override
    public ScanResult<String> scan(String cursor, ScanParams params) {
        return null;
    }

    public RedisMSServer getServer(String key) {
        int num = getHashNum(key);

        for (RedisMSServer redisMSServer : serverLists) {
            if (redisMSServer.contains(num)) {
                return redisMSServer;
            }
        }

        return null;
    }

    private Map<Integer, List<String>> getHashMap(String... keys) {
        Map<Integer, List<String>> hashMap = new HashMap<Integer, List<String>>();
        for (String key : keys) {
            int hash = getHashNum(key);
            for (int i = 0; i < serverLists.size(); i++) {
                if (serverLists.get(i).contains(hash)) {
                    if (!hashMap.containsKey(i)) {
                        hashMap.put(i, new ArrayList<String>());
                    }

                    hashMap.get(i).add(key);
                    break;
                }
            }
        }
        return hashMap;
    }
}
