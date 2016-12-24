package com.liuyiling.coding.redis.util;

import com.liuyiling.common.util.UniversalLogger;

import java.util.zip.CRC32;

/**
 * Created by liuyl on 2016/12/20.
 */
public class HashUtil {

    private static ThreadLocal<CRC32> crc32ThreadLocal = new ThreadLocal<CRC32>(){
        @Override
        protected CRC32 initialValue() {
            return new CRC32();
        }
    };

    public static long getCrc32Hash(String key){

        long hashValue = -1;

        try {
            hashValue = getCrc32(key.getBytes("UTF-8"));
            if(hashValue < 0){
                hashValue = -1 * hashValue;
            }
        } catch (Exception e) {
            UniversalLogger.warn(new StringBuilder(128).append("HashUtil getCrc32Hash error:key=").append(key).toString());
        }

        return hashValue;
    }

    /**
     * 使用 Threadlocal是为了减少大并发场景下new 对象过多
     * @param key
     * @return
     */
    private static long getCrc32(byte[] key){
        CRC32 crc32 = crc32ThreadLocal.get();
        crc32.reset();
        crc32.update(key);
        return crc32.getValue();
    }




}
