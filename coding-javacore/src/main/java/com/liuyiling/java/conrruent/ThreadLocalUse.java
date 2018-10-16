package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/29.
 * 线程本地变量的使用
 * 场景：
 * 对于每个htpp请求,我们需要跟踪进入每个方法时调用的资源数量做计数器计数器,
 * 如果所有的请求共用一个计数器,那么这个计数器是全部请求的调用资源的次数
 * 所以需要使用ThreadLocal来对每个线程存储对应的线程变量
 */
public class ThreadLocalUse {

    //每个ThreadLocal为一个Map类型的数据结构，key为当前线程，value为所需要放入的值
    private static ThreadLocal<Long> longLocals = new ThreadLocal<>();
    private static ThreadLocal<String> stringLocals = new ThreadLocal<>();


    public void set() {
        longLocals.set(Thread.currentThread().getId());
        stringLocals.set(Thread.currentThread().getName());
    }

    public Long getLong() {
        return longLocals.get();
    }

    public String getString() {
        return stringLocals.get();
    }

    public static void main(String[] agrs) {
        ThreadLocalUse localUse = new ThreadLocalUse();
        localUse.set();

        new Thread(() -> {
            localUse.set();
            System.out.println(localUse.getLong());
            System.out.println(localUse.getString());
        }).start();

        System.out.println(localUse.getLong());
        System.out.println(localUse.getString());
    }
}