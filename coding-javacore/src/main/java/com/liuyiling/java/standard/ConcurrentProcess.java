package com.liuyiling.java.standard;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by liuyiling on 17/5/24.
 * 6-并发处理
 */
public class ConcurrentProcess {

    public static void main(String[] args) {
        //1.获取单例对象时需要保证线程安全,其中的方法也要保证线程安全
        //2.创建线程时需要使用有意义的名字
        class TimerTaskThread extends Thread {
        }

        //3.线程池必须由线程池提供,不允许显示创建资源,使用线程池是为了减少线程建立和销毁的时间,提高系统性能
        //4.线程池不允许使用Executors,应该通过ThreadPoolExecutor,主要是为了防止OOM
        //5.SimpleDataFormat是线程不安全的,一定要注意,或者可以使用
        //6.高并发场景下,要尽量缩小锁的范围,能不加锁不加锁,如果非要加锁,考虑代码块/方法/对象/类锁的损耗
        //7.对多个资源进行加锁,需要考虑到加锁的顺序
        //8.并发修改同一个记录的时候,为了解决冲突,必须要加锁,要么在应用层加锁,要么在缓存层加锁,或者对数据库使用乐观锁,当冲突概率小于20%时,使用乐观锁,
        //且重试次数大于3,否则使用悲观锁
        //9.虽然Random是线程安全的,但是高并发下会有竞争seed的问题,应该让每个线程都有一个Random实例
        //10.volatile可以解决变量一写多读的同步问题,但是如果是多写,应该使用
        AtomicInteger count = new AtomicInteger();
        count.addAndGet(1);
    }

    //11.ThreadLocal建议使用static进行修饰,确保所有的子类都共用父类的变量,只在第一次初始化的时候完成
    public static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
}
