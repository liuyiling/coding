package com.liuyiling.java.dynamicProxy;

import com.liuyiling.java.staticProxy.RealSubject;
import com.liuyiling.java.staticProxy.Subject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by liuyl on 2016/11/17.
 */
public class DynamicProxyFactory {

    public static Subject getInstance(){
        Subject delegate = new RealSubject();
        InvocationHandler handler = new SubjectInvocationHandler(delegate);
        Subject proxy = null;
        proxy = (Subject)Proxy.newProxyInstance(
                delegate.getClass().getClassLoader(),
                delegate.getClass().getInterfaces(),
                handler);
        return proxy;
    }

}
