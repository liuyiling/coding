package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadSynchronized {

    public static void main(String[] agrs) {
        InsertData insertData = new InsertData();

        new Thread("线程1") {
            @Override
            public void run() {
                insertData.insert(Thread.currentThread());
            }
        }.start();

        new Thread("线程2") {
            @Override
            public void run() {
                insertData.insert(Thread.currentThread());
            }
        }.start();
    }
}

class InsertData extends Thread {
//    public synchronized void insert(Thread thread) {
//        for (int i = 0; i < 5; i++) {
//            System.out.println(thread.getName() + "插入： " + i);
//        }
//    }

//    public void insert(Thread thread) {
//        synchronized (this) {
//            for (int i = 0; i < 5; i++) {
//                System.out.println(thread.getName() + "插入： " + i);
//            }
//        }
//    }

    private Object object = new Object();
    public void insert(Thread thread) {
        synchronized (object) {
            for (int i = 0; i < 5; i++) {
                System.out.println(thread.getName() + "插入： " + i);
            }
        }
    }
}