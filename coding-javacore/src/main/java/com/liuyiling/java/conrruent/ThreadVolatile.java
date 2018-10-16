package com.liuyiling.java.conrruent;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

/**
 * Created by liuyl on 15/11/29.
 */
public class ThreadVolatile {
    public AtomicInteger anInt = new AtomicInteger(0);

    public void increase() {
        anInt.incrementAndGet();
    }

    public static void main(String[] args) {
        final ThreadVolatile test = new ThreadVolatile();

        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    test.increase();
                }
            }).start();
        }

        while (Thread.activeCount() > 1) {
            //保证前面的线程都执行完
            Thread.yield();
        }
        out.println("inc=" + test.anInt.get());
    }
}
