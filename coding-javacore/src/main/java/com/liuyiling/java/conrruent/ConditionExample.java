package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * Condition与ReentrantLock结合使用
 * 与Object.wait()和Object.notify()类似
 *
 * @author liuyiling
 * @date on 2018/3/8
 */
public class ConditionExample{
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            lock.lock();
            out.println("线程1 获得锁");
            try {
                out.println("线程1 await");
                condition.await();
                out.println("线程1 再次获得锁");
            } catch (InterruptedException e) {
            } finally {
                out.println("线程1 释放锁");
                lock.unlock();
            }

        }).start();

        new Thread(() -> {
            lock.lock();
            out.println("线程2 获得锁");
            try {
                out.println("线程2 notifyAll");
                condition.signalAll();
            } finally {
                out.println("线程2 释放锁");
                lock.unlock();
            }

        }).start();
    }
}
