package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * Created by liuyiling on 2018/3/8
 * 相比synchronized,有以下几个优点:
 * 1.可重入
 * 2.可中断
 * 3.可限时
 * 4.公平锁
 * @author liuyiling
 */
public class ReentrantLockDemo implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            lock.lock();
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockDemo tl = new ReentrantLockDemo();
        Thread t1 = new Thread(tl);
        Thread t2 = new Thread(tl);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        out.println(ReentrantLockDemo.i);
    }
}
