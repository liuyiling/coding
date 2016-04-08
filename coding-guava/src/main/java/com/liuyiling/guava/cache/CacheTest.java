package com.liuyiling.guava.cache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.concurrent.*;

/**
 * Created by liuyl on 16/1/15.
 * 一定要深刻理解expireAfterWrite和refreshAfterWrite才可以正确使用guava
 */
public class CacheTest {

    Executor executor = Executors.newFixedThreadPool(100);

    LoadingCache<String, String> caches = CacheBuilder.newBuilder()
            .maximumSize(100)
            //在没过期的情况下才会监测是否需要刷新
            .expireAfterWrite(10, TimeUnit.SECONDS)//这个方法和load有关
            .refreshAfterWrite(5, TimeUnit.SECONDS)//这个方法和reload有关
                    //.expireAfterAccess(2, TimeUnit.SECONDS)
            .removalListener(new RemovalListener<Object, Object>() {
                @Override
                public void onRemoval(RemovalNotification<Object, Object> removalNotification) {
                    System.out.println(removalNotification.getKey() + " was removed!");
                }
            })
            .build(
                    new CacheLoader<String, String>() {
                        @Override
                        public String load(String s) throws Exception {
                            System.out.println("load " + s);
                            //此刻构建完key对应的value,如果发现缓存容量不够，则先进行缓存的删除，再进行放入
                            return "value " + s;
                        }

                        //先删除原本的值，再放入新的值
                        public ListenableFuture<String> reload(final String key, String value) {

                            ListenableFutureTask<String> task = ListenableFutureTask.create(new Callable<String>() {
                                public String call() {
                                    System.out.println("reload " + key);
                                    return "revalue " + key;
                                }
                            });

                            executor.execute(task);

                            return task;

                        }
                    }
            );
    /**
     * 容量设置为2时，
     * 先进先出引用
     *
     * @throws ExecutionException
     */
    public void test1() throws ExecutionException {
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
        System.out.println(caches.get("c"));
    }


    /**
     * .expireAfterWrite(10, TimeUnit.SECONDS)
     * .refreshAfterWrite(5, TimeUnit.SECONDS)
     * 操作时，检查是否距离上次读写已经10s了，如果是，则过期，重新进行读取load
     * 操作时，检查是否应该刷新，如果是，则进行reload
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void test2() throws ExecutionException, InterruptedException {
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
        System.out.println(caches.get("c"));
        Thread.sleep(8000);

        /**
         * 强制刷新b
         */
        caches.refresh("b");
        /**
         * 当前key的存在时间情况为
         * value a  8s
         * revalue b  0s
         * value c  8s
         */

        /**
         * value c  8s             读c，未过期，但需要刷新，并且返回的值为刷新前
         */
        System.out.println(caches.get("c"));
        Thread.sleep(2000);

        /**
         * 当前key的存在时间情况为
         * value a  10s             不读a,不做任何操作
         * revalue b  2s            读b，但是已经刷新，返回revalue b
         * value c  10s             读c，但是已经过期，返回
         */
        System.out.println(caches.get("b"));
        /**
         * 在写或者读操作的时候顺便维护缓存，此时已经超过10s,过期，重新读取
         */
        System.out.println(caches.get("c"));
    }

    /**
     * .expireAfterWrite(10, TimeUnit.SECONDS)
     * .refreshAfterWrite(5, TimeUnit.SECONDS)
     * get时，检查是否距离上次写已经5s了，如果是，则过期，重新进行读取
     *
     * @throws ExecutionException
     */
    public void test3() throws ExecutionException, InterruptedException {
        System.out.println(caches.get("a"));
        Thread.sleep(1000);
        System.out.println(caches.get("a"));
        Thread.sleep(1000);
        System.out.println(caches.get("a"));
        Thread.sleep(1000);
        System.out.println(caches.get("a"));
        Thread.sleep(1000);
        System.out.println(caches.get("a"));
        Thread.sleep(1000);
        System.out.println(caches.get("a"));
        Thread.sleep(10000);
        System.out.println(caches.get("a"));
    }

    public void test4() throws ExecutionException, InterruptedException {
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
        /**
         * 5s会自动刷新,10s会自动过期
         */
        Thread.sleep(4000);
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
        Thread.sleep(4000);
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
        Thread.sleep(4000);
        System.out.println(caches.get("a"));
        System.out.println(caches.get("b"));
    }

    public static void main(String[] agrs) {
        CacheTest cacheTest = new CacheTest();
        try {
            cacheTest.test2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
