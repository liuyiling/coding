package com.liuyiling.java.conrruent;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by liuyl on 15/11/27.
 */
public class ReadAndWriteLOck {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private ArrayList<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        ReadAndWriteLOck mt = new ReadAndWriteLOck();
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
        lock.readLock().lock();
        try {
            long start = System.currentTimeMillis();

            while(System.currentTimeMillis() - start <= 20) {
                System.out.println(thread.getName()+"正在进行读操作");
            }
            System.out.println(thread.getName()+"读操作完毕");
        } finally {
            lock.readLock().unlock();
        }
    }
}
