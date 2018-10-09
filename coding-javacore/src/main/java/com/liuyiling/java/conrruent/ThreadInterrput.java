package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/27.
 * interrupt只能中断正在sleep的线程
 */
public class ThreadInterrput {

    public static void main(String[] args) {
        ThreadInterrput mh = new ThreadInterrput();
        ThreadTest tt = mh.new ThreadTest();
        tt.start();
        try {
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
        }
        tt.interrupt();
    }

    class ThreadTest extends Thread {
        @Override
        public void run() {
            try {
                System.out.println("子线程睡眠开始");
                Thread.currentThread().sleep(10000);
                System.out.println("子线程睡眠结束");
            } catch (InterruptedException e) {
                System.out.println("子线程获取到中断");
            }
            System.out.println("run执行完毕");
        }
    }
}
