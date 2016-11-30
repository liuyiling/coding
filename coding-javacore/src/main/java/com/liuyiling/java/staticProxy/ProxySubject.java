package com.liuyiling.java.staticProxy;

/**
 * Created by liuyl on 15/12/18.
 * 静态代理类，如果需要对多个类进行代理，17、19行会有代码重复
 * 不是一种好的方法
 */
public class ProxySubject implements Subject{

    private Subject subject;

    public ProxySubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public void dealTask(String taskName) {
        long stime = System.currentTimeMillis();
        //将请求分派给委托类处理
        subject.dealTask(taskName);
        long ftime = System.currentTimeMillis();
        System.out.println("执行任务耗时"+(ftime - stime)+"毫秒");
    }
}
