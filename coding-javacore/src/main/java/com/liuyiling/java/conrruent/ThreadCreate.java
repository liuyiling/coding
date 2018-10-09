package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/27.
 * 创建线程的两种方式
 */
public class ThreadCreate {
    public static void main(String[] args) {
        System.out.println("主线程ID:" + Thread.currentThread().getId());

        /**
         * 第一种创建线程的方式
         */
        Thread firstMethod2CreateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程ID:" + Thread.currentThread().getId());
            }
        });
        firstMethod2CreateThread.start();

        /**
         * 第二种创建线程的方式
         */
        class SecondMethod2CreateThread extends Thread {
            @Override
            public void run() {
                System.out.println("子线程ID:" + Thread.currentThread().getId());
            }
        }
        SecondMethod2CreateThread secondMethod2CreateThread = new SecondMethod2CreateThread();
        secondMethod2CreateThread.start();
    }
}