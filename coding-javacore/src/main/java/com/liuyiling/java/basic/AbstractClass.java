package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/24.
 * 抽象类的学习：
 *  抽象类和普通的类没有什么差别，除了可以定义抽象方法除外
 */
public abstract class AbstractClass {

    //可以有成员变量
    private int a = 0;

    //可以有非抽象方法
    public void method(){
        System.out.println(a);
    }

}

