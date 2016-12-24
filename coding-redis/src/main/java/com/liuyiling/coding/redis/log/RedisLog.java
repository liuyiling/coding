package com.liuyiling.coding.redis.log;


import com.liuyiling.common.util.UniversalLogger;

public class RedisLog {

    /**
     * if consumeTime > REDIS_FIRE_TIME may be log msg
     */
    public static void slowLog(String msg, long consumeTime) {
        slowLog(msg, consumeTime, UniversalLogger.REDIS_FIRE_TIME);
    }

    /**
     * if consumeTime > slowTime may be log msg
     */
    private static void slowLog(String msg, long consumeTime, long slowTime) {
        if (consumeTime >= slowTime) {
            log(msg + " Slow: " + consumeTime);
        }
    }

    /**
     * if consumeTime > slowTime may be log msg
     */
    static void errorLog(String msg, Exception e) {
        log(msg + " ERROR: " + e);
    }

    /**
     * if consumeTime > slowTime may be log msg
     */
    static void errorLog(String msg, Throwable e) {
        log(msg + " ERROR: " + e);
    }

    /**
     * if consumeTime > slowTime may be log msg
     */
    private static void log(String msg) {
        UniversalLogger.fire(msg);
    }
}
