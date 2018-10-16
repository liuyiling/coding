package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by liuyl on 15/11/27.
 */
public class ReadAndWriteLock {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        ReadAndWriteLock readAndWriteLock = new ReadAndWriteLock();
        new Thread(() -> readAndWriteLock.read()).start();
        new Thread(() -> readAndWriteLock.read()).start();
        new Thread(() -> readAndWriteLock.write()).start();
        new Thread(() -> readAndWriteLock.write()).start();
    }

    public void read() {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在进行读操作");
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 10000){}
        } catch (Exception e) {
        } finally {
            System.out.println(Thread.currentThread().getName() + "读操作完毕");
            lock.readLock().unlock();
        }
    }

    public void write() {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在进行写操作");
            long startTime = System.currentTimeMillis();
            while ((System.currentTimeMillis() - startTime) < 10000){}
        } catch (Exception e) {
        } finally {
            System.out.println(Thread.currentThread().getName() + "写操作完毕");
            lock.writeLock().unlock();
        }
    }
}