package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/27.
 */
public class ThreadSleep {

    private int i = 10;
    public Object lock = new Object();

    public static void main(String[] args) {
        ThreadSleep mh = new ThreadSleep();
        ThreadTest tt = mh.new ThreadTest();
        ThreadTest tt2 = mh.new ThreadTest();
        tt.start();
        tt2.start();
    }

    class ThreadTest extends Thread{
        @Override
        public void run() {
            //使用的锁为Object lock = new Object(); 而不是方法所在的类的内置monitor
            synchronized (lock) {
                i++;
                System.out.println("i:" + i);
                try {
                    System.out.println(Thread.currentThread().getId()+"睡眠开始");
                    Thread.currentThread().sleep(1000);
                } catch (Exception e) {
                }finally {
                    System.out.println(Thread.currentThread().getId()+"睡眠结束");
                }
                i++;
                System.out.println("i:" + i);
            }
        }
    }
}
