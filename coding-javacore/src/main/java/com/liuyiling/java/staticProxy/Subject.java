package com.liuyiling.java.staticProxy;

/**
 * Created by liuyl on 15/12/18.
 * 静态代理
 * 优点：业务类只需关注业务本身，把业务无关的作为before或者after方法来运行
 *
 * 缺点：需要为每个类\每个方法都撰写代理类，当接口中的方法增加时，也要撰写代理类，耗时巨大
 */
public interface Subject {

    void dealTask(String taskName);
}
