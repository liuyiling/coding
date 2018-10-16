package com.liuyiling.java.conrruent;

import java.util.concurrent.CountDownLatch;

import static java.lang.System.out;

/**
 * @author liuyiling
 * @date on 2018/10/16
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 0; i < 5; i++) {
            new FirstBatchPassenger(countDownLatch).start();
        }

        for (int i = 0; i < 5; i++) {
            new SecondBatchPassenger(countDownLatch).start();
        }

        while (countDownLatch.getCount() != 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        out.println("MainThread countDown!");
        countDownLatch.countDown();
    }
}

class FirstBatchPassenger extends Thread {
    private CountDownLatch countDownLatch;

    public FirstBatchPassenger(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        out.println("FirstBatchPassenger Executed!");
        countDownLatch.countDown();
    }
}

class SecondBatchPassenger extends Thread {
    private CountDownLatch countDownLatch;

    public SecondBatchPassenger(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        try {
            countDownLatch.await();
            out.println("SecondBatchPassenger Executed!");
        } catch (InterruptedException e) {
        }
    }
}
