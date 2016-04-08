package com.liuyiling.guava.cache;

import com.google.common.cache.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuyl on 16/1/27.
 */
public class CacheTest2 {

    private LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(5l, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                    System.out.println(removalNotification.getKey() + " was removed!");
                }
            })

            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String key) throws Exception {
                    System.out.println("load: " + key);
                    return key + "value";
                }
            });


    public static void main(String[] agrs) throws ExecutionException, InterruptedException {
        CacheTest2 cacheTest2 = new CacheTest2();
        String one = cacheTest2.cache.get("one");

        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
        Thread.sleep(1000);
        one = cacheTest2.cache.get("one");
    }
}
