package com.liuyiling.java.conrruent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadSynchronized2 {

    public static void main(String[] agrs) {

        final InsertData insertData = new InsertData();

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

    private List<Integer> list = new ArrayList<>();

    //在这其实是锁住了InsertData对象的monitor锁
    public synchronized void insert(Thread thread) {

        for (int i = 0; i < 5; i++) {
            System.out.println(thread.getName() + "插入： " + i);
            list.add(i);
        }
    }
}