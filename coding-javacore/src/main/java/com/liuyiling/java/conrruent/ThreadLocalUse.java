package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 15/11/29.
 * 线程本地变量的使用
 * 场景：
 * 1.对于每个htpp请求,我们需要跟踪进入每个方法时调用的资源数量-计数器,如果所有的请求共用一个计数器，那么这个计数器是全部请求的调用资源的次数
 *  所以需要使用ThreadLocal来对每个线程存储对应的线程变量
 */
public class ThreadLocalUse {

    //每个ThreadLocal为一个Map类型的数据结构，key为当前线程，value为所需要放入的值
    private ThreadLocal<Long> longLocals = new ThreadLocal<>();
    private ThreadLocal<String> stringLocals = new ThreadLocal<>();

    public void set(){
        longLocals.set(Thread.currentThread().getId());
        stringLocals.set(Thread.currentThread().getName());
    }

    public Long getLong(){
        return longLocals.get();
    }

    public String getString(){
        return stringLocals.get();
    }



    private static final ThreadLocal<Session> threadSession = new ThreadLocal() {
        @Override
        public Object get() {
            return new Session("liuyiling");
        }
    };

    public static void main(String[] agrs){

        ThreadLocalUse localUse = new ThreadLocalUse();

        localUse.set();
        System.out.println(localUse.getLong());
        System.out.println(localUse.getString());

        Thread thread = new Thread(){
            @Override
            public void run() {
                localUse.set();
                System.out.println(localUse.getLong());
                System.out.println(localUse.getString());
            }
        };

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        System.out.println(localUse.getLong());
        System.out.println(localUse.getString());





        //LocalThreadUseBySession localThreadUseBySession = new LocalThreadUseBySession();

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                //localThreadUseBySession.getSession("thread2");
                //System.out.println(localThreadUseBySession.getSession("thread2"));
                System.out.println(threadSession.get());
                //localThreadUseBySession.getSession("thread2");
                //System.out.println(localThreadUseBySession.getSession("thread2"));
            }
        };
        thread2.start();

        Thread thread3 = new Thread(){
            @Override
            public void run() {
                //localThreadUseBySession.getSession("thread3");
                //System.out.println(localThreadUseBySession.getSession("thread3"));
                System.out.println(threadSession.get());
            }
        };
        thread3.start();


    }


}


class LocalThreadUseBySession{

    private static final ThreadLocal<Session> threadSession = new ThreadLocal() {
        @Override
        public Object get() {
            return new Session("liuyiling");
        }
    };
    private static Session s = null;

    public static Session getSession(String name){
        Session s = threadSession.get();

        //每个线程访问的session对象还是同一个,还是会带来并发访问的问题
        if(s == null){
            Session temp = newSession();
            threadSession.set(temp);
            System.out.println(name + " " + temp.toString());
        }

        //正确地使用方式应该如下：
        //if(s == null){
        //    threadSession.set(new Session("aaa"));
        //}

        return s;
    }


    public static Session newSession(){
        if(s == null){
            s = new Session("liuyiling");
        }
        return s;
    }


}

class Session{

    private String name;

    public Session(String name) {
        this.name = name;
    }
}
