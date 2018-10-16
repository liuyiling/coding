package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadLock {

    private ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ThreadLock mt = new ThreadLock();
        new Thread(() -> mt.insert()).start();
        new Thread(() -> mt.insert()).start();
    }

    public void insert() {
        lock.lock();
        try {
            //睡眠10s，等待锁的线程此时挂起
            System.out.println(Thread.currentThread().getName() + "获得锁");
            Thread.currentThread().sleep(10000);
        } catch (Exception e) {
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放锁");
            lock.unlock();
        }
    }
}
