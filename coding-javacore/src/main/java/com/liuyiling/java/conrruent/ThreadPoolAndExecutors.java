package com.liuyiling.java.conrruent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.System.out;

/**
 * Created by liuyl on 15/11/25.
 * 练习使用线程池
 * 线程池对于限制再一个程序中的线程数量有很大的作用
 * 比如：
 * kafka消费者取到消息之后，不会无限地开启线程去进行消费，而是把线程扔进线程池之后，就不管了，这样可以带来
 * 1.异步处理的高并发获取消息  2.使用线程池控制数量
 */
public class ThreadPoolAndExecutors {

    public static void main(String[] agrs) throws ExecutionException, InterruptedException {
        //根据需要可以创建新线程的线程池。线程池中曾经创建的线程，在完成某个任务后也许会被用来完成另外一项任务。
        ExecutorService executor = Executors.newCachedThreadPool();
        //创建一个可重用固定线程数的线程池。这个线程池里最多包含nThread个线程。
        ExecutorService executor2 = Executors.newFixedThreadPool(10);
        //创建一个使用单个 worker 线程的 Executor。即使任务再多，也只用1个线程完成任务。
        ExecutorService executor3 = Executors.newSingleThreadExecutor();
        //创建一个单线程执行程序，它可安排在给定延迟后运行命令或者定期执行。
        ExecutorService executor4 = Executors.newSingleThreadScheduledExecutor();

        /**
         * 无返回值的线程
         */
        for (int i = 0; i < 5; i++) {
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    out.println("子线程ID:" + Thread.currentThread().getId());
                }
            });
        }
        //启动一次顺序关闭，执行以前提交的任务，但不接受新任务。执行此方法后，线程池等待任务结束后就关闭，同时不再接收新的任务。如果执行完shutdown()方法后，再去执行execute方法则直接抛出RejectedExecutionException
        executor.shutdown();


        /**
         * 有返回值的线程
         */
        class CalcTask implements Callable<String> {
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName();
            }
        }
        /**
         * ------第一种写法------
         */
        //新建任务列表
        List<Callable<String>> taskList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            taskList.add(new CalcTask());
        }
        try {
            //invokeAll开始执行任务列表，并等待所有任务都执行完才返回
            //Future表示异步计算的结果
            List<Future<String>> futureList = executor.invokeAll(taskList);
            //停止接受任务
            executor.shutdown();
            for(Future<String> future : futureList){
                String futureResult = future.get();
                out.println(futureResult);
            }
        } catch (Exception e) {
        }

        /**
         * ------第二种写法------
         */
        List<Future> futureList = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            //提交任务，提交后立即开始执行
            Future<String> future = executor.submit(new CalcTask());
            futureList.add(future);
        }
        //停止接受任务
        executor.shutdown();
        for(Future<String> future : futureList){
            while (!future.isDone()){
                Thread.sleep(100);
            }
            String futureResult = future.get();
            out.println(futureResult);
        }
    }
}