package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * Created by liuyiling on 2018/3/8
 *
 * @author liuyiling
 */
public class ReentrantLockInterrupt implements Runnable {

    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;

    public ReentrantLockInterrupt(int lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        try {
            if (lock == 1) {
                lock1.lockInterruptibly();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                lock2.lockInterruptibly();
            } else {
                lock2.lockInterruptibly();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                lock1.lockInterruptibly();
            }
        } catch (Exception e) {
        } finally {
            if (lock1.isHeldByCurrentThread()) {
                lock1.unlock();
            }
            if (lock2.isHeldByCurrentThread()) {
                lock2.unlock();
            }
            out.println(Thread.currentThread().getId() + ":Thread Out");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockInterrupt r1 = new ReentrantLockInterrupt(1);
        ReentrantLockInterrupt r2 = new ReentrantLockInterrupt(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        Thread.sleep(1000);
    }
}
