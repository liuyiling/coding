package com.liuyiling.guava.cache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

/**
 * Created by liuyl on 16/1/15.
 * 通常来说，Guava Cache适用于：
 * <p>
 * 1.你愿意消耗一些内存空间来提升速度。
 * 2.你预料到某些键会被查询一次以上。
 * 3.缓存中存放的数据总量不会超出内存容量。（Guava Cache是单个应用运行时的本地缓存。它不把数据存放到文件或外部服务器。如果这不符合你的需求，请尝试Memcached这类工具）
 * 4.这些数据不需要分布式一致
 * <p>
 * Guava Cache提供了三种基本的缓存回收方式：基于容量回收、定时回收和基于引用回收。
 */
public class Cache {

    LoadingCache<String, String> caches = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1l, TimeUnit.SECONDS) //缓存项在给定时间内没有被读/写访问，则回收。请注意这种缓存的回收顺序和基于大小回收一样。
            .expireAfterWrite(1l, TimeUnit.SECONDS) //缓存项在给定时间内没有被写访问（创建或覆盖），则回收。如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                    System.out.println(removalNotification.getKey() + " was removed!");
                }
            })
            .build(
                    new CacheLoader<String, String>() {
                        //build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                        @Override
                        public String load(String key) throws Exception {
                            return "hello " + key + "!";
                        }

                        @Override
                        public ListenableFuture<String> reload(String key, String oldValue) throws Exception {

                            ListenableFutureTask<String> task =
                                    ListenableFutureTask.create(new Callable<String>() {
                                        @Override
                                        public String call() throws Exception {
                                            return getValue(key);
                                        }
                                    });
                            ExecutorService executorService = Executors.newFixedThreadPool(20);
                            executorService.submit(task);

                            return task;
                        }
                    }


            );


    public static void main(String[] agrs){

        Cache cache = new Cache();
        try {
            cache.test();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void test() throws ExecutionException {

        System.out.println("a value: " + caches.get("a"));
        System.out.println("b value: " + caches.get("b"));
        System.out.println("c value: " + caches.get("c"));


        /**
         * 指定特殊的字符
         */
        System.out.println("d value: " + caches.get("d",new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getValue("key");
            }
        } ));

    }


    private String getValue(String key) {
        return key + "value";
    }



}
