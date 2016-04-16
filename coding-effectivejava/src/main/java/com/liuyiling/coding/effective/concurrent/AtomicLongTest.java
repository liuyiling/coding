package com.liuyiling.coding.effective.concurrent;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuyl on 16/1/12.
 * Atomic的使用方法：
 *  我们说过，对于int = 1;这样的算术为原子操作，
 *  对于i++;这样的算术并不是原子操作，需要对该原子操作进行同步方法的封装,如
 *  这样即可以把该操作变成原子性的，也保证了线程之间的通信效果
 *  public void synchronized increment(){
 *      i++;
 *  }
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

