package com.liuyiling.guava.concurrent;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuyl on 16/4/7.
 * 对jdk原生的Future的扩展
 */
public class ListenableFutureUse {

    public static void main(String[] agrs) throws ExecutionException, InterruptedException {

        /**
         *  我们知道future表示一个异步执行任务，当任务完成的时候可以获取计算结果
         *  如果我们希望一旦计算完成就立即返回给用户或者调用其他的函数，就必须使用另一个线程不断的查询计算状态
         *  这样做的话，代码效率低下，复杂
         */

        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());

        ListenableFuture<Integer> listenableFuture = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("call execute..");
                TimeUnit.SECONDS.sleep(10);
                return 1;
            }
        });

        Futures.addCallback(listenableFuture, new FutureCallback<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.println("success");
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("fail");
            }
        });

        Integer integer = listenableFuture.get();

        System.out.println("contining");
    }
}
