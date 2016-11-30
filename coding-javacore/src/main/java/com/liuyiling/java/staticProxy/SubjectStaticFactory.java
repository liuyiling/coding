package com.liuyiling.java.staticProxy;

/**
 * Created by liuyl on 2016/11/17.
 */
public class SubjectStaticFactory {

    public static Subject getInstance(){
        return new ProxySubject(new RealSubject());
    }

}
