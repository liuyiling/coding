package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/24.
 * 接口一定是抽象的：
 * 只能包含static final方法和abstract方法
 */
abstract interface Interface {

    //修饰符就算不写也可以
    public static final int INT_A = 0;
    public abstract void fun();

}
