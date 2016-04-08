package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadLock {

    private ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        ThreadLock mt = new ThreadLock();
        new Thread(){
            @Override
            public void run() {
                mt.insert(Thread.currentThread());
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                mt.insert(Thread.currentThread());
            }
        }.start();
    }

    public void insert(Thread thread){
        //获得锁
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+"获得锁");
        } catch (Exception e) {

        //一定要在这边释放锁
        }finally {
            System.out.println(Thread.currentThread().getName()+"释放锁");
            lock.unlock();
        }
    }

}
