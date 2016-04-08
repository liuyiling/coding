package com.liuyiling.coding.effective.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 16/1/12.
 * Atomic的使用方法：
 *  解决并发问题尽量使用atomic原子类
 */
public class AtomicLongTest {

    private static final AtomicLong nextSerialNum = new AtomicLong();

    public static void main(String[] agrs){
        long result = generateSerialNumber();
        System.out.println(result);
    }

    public static long generateSerialNumber(){

        nextSerialNum.getAndIncrement();
        nextSerialNum.compareAndSet(1,2);
        /**
         *  避免了使用 long nextSerialNum; nextSerialNum++的非原子性
         */
        return nextSerialNum.getAndIncrement();
    }

}

