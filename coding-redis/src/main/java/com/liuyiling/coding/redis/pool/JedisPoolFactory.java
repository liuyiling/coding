package com.liuyiling.coding.redis.pool;

import com.liuyiling.common.util.UniversalLogger;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuyl on 2016/12/20.
 */
public class JedisPoolFactory {

    private static ConcurrentHashMap<String, JedisPool> jedisPools = new ConcurrentHashMap<>();

    public static JedisPool getPool(GenericObjectPoolConfig config, URI uri){

        //因为redis节点的数量一般不会超过8个,为了加快性能,该处可以使用intern
        synchronized (uri.toString().intern()){
            JedisPool pool = jedisPools.get(uri.toString());
            if(pool == null){
                pool = new JedisPool(config, uri);
                jedisPools.put(uri.toString(), pool);
                UniversalLogger.info("JedisPoolFactory create pool by server:" + uri.toString());
            } else {
                UniversalLogger.info("JedisPoolFactory reuse pool by server:" + uri.toString());
            }
            return pool;
        }
    }

}
