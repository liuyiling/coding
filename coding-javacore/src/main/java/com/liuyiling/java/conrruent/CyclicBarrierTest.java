package com.liuyiling.java.conrruent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static java.lang.System.out;

/**
 * @author liuyiling
 * @date on 2018/10/16
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> out.println("Action GO!"));
        for (int i = 0; i<6;i++){
            new CyclicBarrierWorker(cyclicBarrier).start();
        }
    }
}

class CyclicBarrierWorker extends Thread {
    private CyclicBarrier cyclicBarrier;
    public CyclicBarrierWorker(CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
        out.println("Executed!");
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
        } catch (BrokenBarrierException e) {
        }
    }
}