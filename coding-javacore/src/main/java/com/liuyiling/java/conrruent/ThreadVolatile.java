package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/29.
 */
public class ThreadVolatile {
    public volatile int inc = 0;

    public void increase() {
        inc++;
    }

    public static void main(String[] args) {
        final ThreadVolatile test = new ThreadVolatile();


        for(int i=0;i<10;i++){
            new Thread(){
                public void run() {
                    for(int j=0;j<10;j++)
                        test.increase();
                }
            }.start();
        }

        while(Thread.activeCount()>1)  //保证前面的线程都执行完
            Thread.yield();

        System.out.println("hehe");
    }
}
