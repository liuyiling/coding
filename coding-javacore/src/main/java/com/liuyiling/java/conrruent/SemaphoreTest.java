package com.liuyiling.java.conrruent;

import java.util.concurrent.Semaphore;

import static java.lang.System.out;

/**
 * Java 提供了经典信号量（Semaphore）的实现，通过控制一定数量的允许（permit）的方式，来达到限制通用资源访问的目的。
 * 你可以想象一下这个场景，在车站、机场等出租车时，当很多空出租车就位时，为防止过度拥挤，调度员指挥排队，等待坐车的队伍一次进来5个人上车，
 * 等这5个人坐车出发，再放进去下一批
 *
 * @author liuyiling
 * @date on 2018/10/16
 */
public class SemaphoreTest {
    private static Semaphore semaphore = new Semaphore(5);

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new CarWorker(semaphore).start();
        }
    }
}

class CarWorker extends Thread {
    private Semaphore semaphore;

    public CarWorker(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            out.println(Thread.currentThread() + " waiting permitted");
            semaphore.acquire();
            out.println(Thread.currentThread() + " on car !");
            Thread.sleep(10000);
        } catch (Exception e) {
        } finally {
            out.println(Thread.currentThread() + " release permitted");
            semaphore.release();
        }
    }
}
