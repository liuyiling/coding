package com.liuyiling.java.conrruent;

import java.io.IOException;

/**
 * Created by liuyl on 15/11/27.
 *  调用了thread.join需要等到thread的run方法执行完毕，主线程才可以继续执行
 */
public class ThreadJoin {

    public static void main(String[] args) throws IOException {
        System.out.println("进入线程"+Thread.currentThread().getName());
        ThreadJoin test = new ThreadJoin();
        MyThread thread1 = test.new MyThread();
        thread1.start();
        try {
            System.out.println("线程"+Thread.currentThread().getName()+"等待");
            thread1.join();
            System.out.println("线程"+Thread.currentThread().getName()+"继续执行");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("进入线程"+Thread.currentThread().getName());
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO: handle exception
            }
            System.out.println("线程"+Thread.currentThread().getName()+"执行完毕");
        }
    }
}
