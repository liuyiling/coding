package com.liuyiling.java.dynamicProxy;

import com.liuyiling.java.staticProxy.Subject;
import com.liuyiling.java.staticProxy.RealSubject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liuyl on 15/12/18.
 * 动态代理类，更具有拓展性
 */
public class DynamicProxyClass {

    public static void main(String[] agrs) {
        Subject instance = DynamicProxyFactory.getInstance();
        instance.dealTask("dealTask");
    }

}

