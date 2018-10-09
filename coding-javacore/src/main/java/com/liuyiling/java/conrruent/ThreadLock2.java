package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadLock2 {

    private ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ThreadLock2 mt = new ThreadLock2();
        new Thread(() -> mt.insert(Thread.currentThread())).start();
        new Thread(() -> mt.insert(Thread.currentThread())).start();
    }

    public void insert(Thread thread) {

        if (lock.tryLock()) {
            try {
                System.out.println(thread.getName() + "得到了锁");
            } catch (Exception e) {
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(thread.getName() + "释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName() + "获取锁失败");
        }


    }
}
