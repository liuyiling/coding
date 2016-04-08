package com.liuyiling.java.conrruent;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * Created by liuyl on 15/11/25.
 * 练习使用线程池
 * 线程池对于限制再一个程序中的线程数量有很大的作用
 * 比如：
 *  kafka消费者取到消息之后，不会无限地开启线程去进行消费，而是把线程扔进线程池之后，就不管了，这样可以带来
 *  1.异步处理的高并发获取消息  2.使用线程池控制数量
 */
public class ThreadPoolTest {

    public static void main(String[] agrs){

        /**
         * 使用guava可以很方便的帮助调试
         */
        final ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Orders-%d")
                .setDaemon(true)
                .build();
        final ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory);



        //并不提倡用这种方法来创建线程
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(5,5, 10L,TimeUnit.SECONDS,
                        new LinkedBlockingDeque<>(5), new ThreadPoolExecutor.DiscardPolicy());

        //使用静态方法来创建线程
        ThreadPoolExecutor threadPoolExecutor1 = (ThreadPoolExecutor) Executors.newCachedThreadPool();


        for(int i = 0; i < 15; i++) {
            MyTask myTask = new MyTask(i);
            executorService.execute(myTask);
            System.out.println("poolSize: " + threadPoolExecutor.getPoolSize()
            + "  queue:  " + threadPoolExecutor.getQueue().size()+ "   CompletedTaskCount:  " + threadPoolExecutor.getCompletedTaskCount());
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.shutdown();
    }
}

class MyTask implements Runnable{

    private int taskNum;

    public MyTask(int taskNum) {
        this.taskNum = taskNum;
    }

    @Override
    public void run() {
        System.out.println("正在执行任务: " + taskNum);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("任务执行完毕: " + taskNum);

    }
}
