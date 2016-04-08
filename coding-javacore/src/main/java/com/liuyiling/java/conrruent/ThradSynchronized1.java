package com.liuyiling.java.conrruent;

import java.util.ArrayList;

/**
 * Created by liuyl on 15/11/27.
 * synchronized锁住特定的监视器
 */
public class ThradSynchronized1 {

    private Object lock = new Object();
    private ArrayList<Integer> list = new ArrayList<>();

    public static void main(String[] args) {
        ThradSynchronized1 mh = new ThradSynchronized1();
        ThreadTest tt = mh.new ThreadTest();
        ThreadTest tt2 = mh.new ThreadTest();
        tt.start();
        tt2.start();
    }

    class ThreadTest extends Thread{
        @Override
        public void run() {
            synchronized (lock) {
                for(int i=0;i<5;i++){
                    list.add(i);
                    System.out.println(Thread.currentThread().getName()
                            + "insert:" +i);
                }
            }
        }
    }
}
