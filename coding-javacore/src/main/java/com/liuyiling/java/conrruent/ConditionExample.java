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
public class ConditionExample implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            condition.await();
            out.println("Thread is going on");
        } catch (Exception e) {
            out.println("Thread is interrupt");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionExample c1 = new ConditionExample();
        Thread t1 = new Thread(c1);
        t1.start();
        Thread.sleep(1000);
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
