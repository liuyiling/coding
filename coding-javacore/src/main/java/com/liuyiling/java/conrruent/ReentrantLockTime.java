package com.liuyiling.java.conrruent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * ReentrantLock的可限时特性
 *
 * @author liuyiling
 */
public class ReentrantLockTime implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                Thread.sleep(6000);
            } else {
                out.println("get lock failed");
            }
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ReentrantLockTime r1 = new ReentrantLockTime();
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r1);
        t1.start();
        t2.start();
    }
}
