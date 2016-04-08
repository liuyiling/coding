package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/27.
 * 创建线程的两种方式
 */
public class ThreadCreate {

    //方式1：继承 Thread
    //public static void main(String[] args) {
    //    System.out.println("主线程ID:" + Thread.currentThread().getId());
    //    TestThread tt = new TestThread("子线程1");
    //    tt.start();
    //    TestThread tt2 = new TestThread("子线程2");
    //    tt2.start();
    //    TestThread tt3 = new TestThread("子线程2");
    //    tt3.start();
    //}

    public static void main(String[] args) {
        System.out.println("主线程ID:" + Thread.currentThread().getId());
        TestThread run = new TestThread();
        Thread t = new Thread(run);
        Thread t2 = new Thread(run);
        t.start();
        t2.start();
    }
}


//class TestThread extends Thread{
//
//    private String name;
//
//    public TestThread(String name) {
//        super(name);
//        this.name = name;
//    }
//    @Override
//    public void run() {
////		在此书写线程的执行代码
//        System.out.println(name + "子线程ID:" + Thread.currentThread().getId());
//    }
//}

/**
 * 方式2：实现runnable接口
 */
class TestThread implements Runnable{

    @Override
    public void run() {
//		在此书写线程的执行代码
        System.out.println("子线程ID:" + Thread.currentThread().getId());
    }
}