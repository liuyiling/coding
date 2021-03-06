package com.liuyiling.java.io;

/**
 * Created by liuyl on 16/6/7.
 * 为解决大并发下的性能问题，常常会使用NIO方式来编写服务代码，也叫reactor模式
 *
 * 以餐厅服务为例：
 * 传统：来一个客人（请求），派一个服务员（线程）进行服务，来5个客人，就有5个服务员，来10个客人，就有10个服务员
 * 线程池：生意越来越好，有了20个客人，可是请20个服务员的话，餐厅就赚不到钱啦，于是老板雇了一个10个服务员的线程池，轮流服务客人。可是有的客人点菜很慢，占用了服务员，导致其他的客人等待很久
 *  还得不到服务，怒气冲冲地走了
 * NIO:老板发现，大部分服务员都是在等待客人点菜，其实干的活不多。于是老板发明了一个新方法：当客人点菜的时候，服务员就可以去服务其他客人，等客人点好菜直接招呼一声服务员，马上就有服务员过去服务，
 *  从此，餐厅只剩下了一个服务员
 *
 */
public class ReatorNio {



}
