package com.liuyiling.coding.effective.concurrent;

import java.util.concurrent.*;

/**
 * Created by liuyl on 16/1/12.
 * Excutor和Task优先于new Thread使用
 * 不要自己编写work queue用来做异步处理的线程池,也尽量不使用线程
 * 以前的Thread既承担了工作任务(业务逻辑)，又承担了执行任务(启动和结束)
 * Runnable（无返回结果） 和 Callable(有返回结果) 承担了工作任务 现在不同了,executor Service承担执行任务
 */
public class ExecutorTest {

    public static void main(String[] agrs){

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while( i < 10){
                    i++;
                    System.out.println(i);
                }
            }
        });

        Future future = executor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                int i = 0;
                while (i < 10) {
                    i++;
                }
                return i;
            }
        });

        try {
            System.out.println("result: "  + future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
