package com.liuyiling.coding.effective.concurrent;

/**
 * Created by liuyl on 16/1/11.
 * 同步访问共享的可变数据需要注意的问题：
 * 1.尽量保证可变数据在单个线程中，也就是说，多线程之间不共享可变的数据
 * 2.当多个线程共享可变数据的时候，每个读者或者写数据的线程都必须执行同步，如果没有同步，就无法保证一个线程所做的修改能够被其他线程感知
 */
public class StopThread {

    //多线程共享了可变的数据
    private static boolean isStop = false;

    public static void main(String[] agrs) {

        Thread t1 = new Thread() {
            int i = 0;

            @Override
            public void run() {
                /**
                 * 这部分代码会自动被JVM优化成
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

        /**
         * 时间已经过去了1s，但是后台线程并不会停止
         */
        isStop = true;
        System.out.println("down");
    }

}


/**
 * 只有将对共享数据的写方法和读方法都进行同步,程序才能进行正确的同步
 */
class StopThread2 {

    private static boolean isStop = false;

    /**
     * 这边的同步只是为了通信效果，为了让其他线程能及时看到修改后的值，而不是为了互斥访问
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
 * 和StopThread2一样，也可以实现1s后，后台线程停止
 * 使用volatile一样可以保证同步效果，或者说通信效果（因为这边的同步仅仅是为了让其他线程可已及时看到修改后的值)
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
