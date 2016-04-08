package com.liuyiling.spring;

/**
 * Created by liuyl on 15/12/29.
 */
public class A {

    B b;

    public void setB(B b) {
        this.b = b;
    }

    public void test(){
        b.b();
    }
}
