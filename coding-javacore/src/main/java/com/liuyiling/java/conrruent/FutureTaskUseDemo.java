package com.liuyiling.java.conrruent;

import java.util.concurrent.*;

import static java.lang.System.out;

/**
 * 使用Callable和Future执行异步处理并获取结果
 *
 * @author liuyiling
 */
public class FutureTaskUseDemo {

    public static void main(String[] agrs) throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newCachedThreadPool();

        /**
         * 使用FutureTask的Runnable特性
         */
        Thread thread = new Thread(new FutureTask<>(new BoilWater()));
        thread.start();

        /**
         * 使用FutureTask的Callable特性------第一种写法
         */
        FutureTask futureTask = new FutureTask(new BoilWater());
        executor.submit(futureTask);

        out.println("做饭");
        Thread.sleep(2000);
        out.println("饭做好了");

        while (!futureTask.isDone()) {
            out.println("水还没烧开呢");
            Thread.sleep(1000);
        }

        out.println(futureTask.get());


        /**
         * 使用FutureTask的Callable特性------第二种写法
         */
        Future<String> submit = executor.submit(new BoilWater());

        out.println("做饭");
        Thread.sleep(2000);
        out.println("饭做好了");

        while (!submit.isDone()) {
            out.println("水还没烧开呢");
            Thread.sleep(1000);
        }

        out.println(submit.get());

        /**
         * 多个任务同时并行
         */
        CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(executor);
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            cs.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    return taskId;
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            try {
                out.println(cs.take().get());
            } catch (Exception e) {

            }
        }
    }
}

class BoilWater implements Callable<String> {
    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
        return System.currentTimeMillis() + " 水烧开了";
    }
}
