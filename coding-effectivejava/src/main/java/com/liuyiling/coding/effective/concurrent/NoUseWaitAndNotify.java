package com.liuyiling.coding.effective.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuyl on 16/1/12.
 * 淘汰掉wait和notify吧,那已经过时了
 * 多使用concurrent包中的三大宝剑：
 * 1.executor 2.concurrent collection 3.synchronizer
 */
public class NoUseWaitAndNotify {

    /**
     * 并发包
     */
    public static void main(String[] agrs) {
        testProducerAndConsumer();
    }


    public static void testProducerAndConsumer() {

        final ConsumerAndProducer consumerAndProducer = new ConsumerAndProducer();

        class Producer implements Runnable {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("produce apple begin" + System.currentTimeMillis());
                        consumerAndProducer.produce();
                        System.out.println("produce apple end" + System.currentTimeMillis());
                        Thread.sleep(300);
                    }
                } catch (Exception e) {
                }
            }
        }

        class Consumer implements Runnable {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("consume apple begin" + System.currentTimeMillis());
                        consumerAndProducer.consume();
                        System.out.println("consume apple end" + System.currentTimeMillis());
                        Thread.sleep(10000);
                    }
                } catch (Exception e) {
                }
            }
        }


        ExecutorService service = Executors.newCachedThreadPool();
        Producer producer = new Producer();
        Consumer consumer = new Consumer();
        service.submit(producer);
        service.submit(consumer);

        try{
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        service.shutdownNow();

    }



    public static class ConsumerAndProducer {

        /**
         * 生产者消费者队列
         */
        BlockingQueue<String> bag = new ArrayBlockingQueue<String>(3);

        public void produce() throws InterruptedException {
            bag.put("pen");
        }

        public String consume() throws InterruptedException {
            return bag.take();
        }


    }

}
