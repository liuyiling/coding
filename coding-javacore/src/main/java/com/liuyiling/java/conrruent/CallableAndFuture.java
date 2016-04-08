package com.liuyiling.java.conrruent;

import java.util.concurrent.*;

/**
 * Created by liuyl on 15/12/9.
 * 使用Callable和Future获取执行结果
 */
public class CallableAndFuture {


    public static void main(String[] agrs){

        /**
         * 第一种方式
         */
        ExecutorService executor = Executors.newCachedThreadPool();
        Task task = new Task();
        //Future<Integer> result = executor.submit(task);
        //executor.shutdown();


        /**
         * 下面是第二种方式
         */
        FutureTask<Integer> futureTask = new FutureTask<>(task);
        executor.submit(futureTask);
        executor.shutdown();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main is running");

        try {
            //System.out.println(result.get());
            //阻塞等待获取
            System.out.println(futureTask.get());
            //非阻塞等待获取
            try {
                System.out.println(futureTask.get(1000L, TimeUnit.MICROSECONDS));
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            //处理终端异常
            e.printStackTrace();
        } catch (ExecutionException e) {
            //处理无法执行任务异常
            e.printStackTrace();
        }

        System.out.println("all thread is end");

    }

}

class Task implements Callable<Integer>{

    @Override
    public Integer call() throws Exception {
        System.out.println("task is running");
        Thread.sleep(10000);
        int sum = 0;
        for(int i = 0; i < 100; i++){
            sum += i;
        }
        return sum;
    }
}
