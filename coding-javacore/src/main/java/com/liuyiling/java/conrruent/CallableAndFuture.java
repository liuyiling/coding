package com.liuyiling.java.conrruent;

import java.util.concurrent.*;

/**
 * Created by liuyl on 15/12/9.
 * 使用Callable和Future执行异步处理并获取结果
 */
public class CallableAndFuture {

    public static void main(String[] agrs) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask futureTask = new FutureTask(new BoilWater());
        executor.submit(futureTask);

        System.out.println("做饭");
        Thread.sleep(2000);
        System.out.println("饭做好了");

        while ( !futureTask.isDone() ) {
            System.out.println("水还没烧开呢");
            Thread.sleep(1000);
        }

        System.out.println(futureTask.get());


        /**
         * 第二种方式
         */
        Future<String> submit = executor.submit(new BoilWater());

        System.out.println("做饭");
        Thread.sleep(2000);
        System.out.println("饭做好了");

        while ( !submit.isDone() ) {
            System.out.println("水还没烧开呢");
            Thread.sleep(1000);
        }

        System.out.println(submit.get());
    }
}

class BoilWater implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return System.currentTimeMillis() + " 水烧开了";
    }
}
