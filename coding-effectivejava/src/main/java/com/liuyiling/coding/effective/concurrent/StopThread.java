package com.liuyiling.coding.effective.concurrent;

/**
 * Created by liuyl on 16/1/11.
 * 没有同步的代码块
 * 当多个共享的可变数据的时候，每个读者或者写数据的线程都必须执行同步
 * 如果没有同步，就无法保证一个线程所做的修改能够被其他线程感知
 */
public class StopThread {

    private static boolean isStop = false;

    public static void main(String[] agrs) {

        Thread t1 = new Thread() {
            int i = 0;

            @Override
            public void run() {
                /**
                 * 这部分代码会自动同步成
                 * if( !isStop ){
                 *  while(true){
                 *  }
                 * }所以看不到打印end
                 */
                while (!isStop) {
                    i++;
                }
                System.out.println("end");
            }
        };

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isStop = true;
        System.out.println("down");
    }

}


/**
 * 只有将写方法和读方法都同步,程序才能按照我们的意思来
 */
class StopThread2 {

    private static boolean isStop = false;

    /**
     * 这些方法即使不是同步的，也是具有原子性的，
     * 这边的同步只是为了通信效果，而不是为了互斥访问
     */
    private static synchronized void stopRequested() {
        isStop = true;
    }

    private static synchronized boolean isStop() {
        return isStop;
    }

    public static void main(String[] agrs) {

        Thread t1 = new Thread() {

            int i = 0;

            @Override
            public void run() {
                while (!isStop()) {
                    i++;
                }
                System.out.println("end");
            }
        };

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stopRequested();
    }

}


/**
 * 使用volatile一样可以保证可见性
 */
class StopThread3 {

    private static volatile boolean isStop = false;

    public static void main(String[] agrs) {

        Thread t1 = new Thread() {
            int i = 0;

            @Override
            public void run() {
                while (!isStop) {
                    i++;
                }
                System.out.println("end");
            }
        };

        t1.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isStop = true;
    }

}
