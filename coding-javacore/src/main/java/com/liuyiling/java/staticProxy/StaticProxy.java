package com.liuyiling.java.staticProxy;

/**
 * Created by liuyl on 2016/11/17.
 */
public class StaticProxy {

    public static void main(String[] agrs){
        Subject instance = SubjectStaticFactory.getInstance();
        instance.dealTask("dealTask");
    }

}
