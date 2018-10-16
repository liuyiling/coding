package com.liuyiling.java.conrruent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadLock3 {

    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        MyThread thread1 = new MyThread("1", lock);
        MyThread thread2 = new MyThread("2", lock);
        thread1.start();
        Thread.sleep(100);
        thread2.start();
        Thread.sleep(1000);
        thread2.interrupt();
    }
}

class MyThread extends Thread {
    private Lock lock = null;

    public MyThread(String name, Lock lock) {
        super(name);
        this.lock = lock;
    }

    @Override
    public void run() {
        //注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将InterruptedException抛出
        try {
            lock.lockInterruptibly();
            out.println(this.getName() + "得到了锁");
            long startTime = System.currentTimeMillis();
            for (; ; ) {
                if (System.currentTimeMillis() - startTime >= Integer.MAX_VALUE) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            out.println(Thread.currentThread().getName() + "被中断");
        } finally {
            out.println(Thread.currentThread().getName() + "执行finally");
        }
    }
}

