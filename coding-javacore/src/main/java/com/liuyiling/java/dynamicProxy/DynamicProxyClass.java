package com.liuyiling.java.dynamicProxy;

import com.liuyiling.java.staticProxy.Calculator;
import com.liuyiling.java.staticProxy.CalsulatorImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by liuyl on 15/12/18.
 * 动态代理类，更具有拓展性
 */
public class DynamicProxyClass {


    public void testDynamicProxy() {

        Calculator calculator = new CalsulatorImpl();
        LogHandler logHandler = new LogHandler(calculator);
        Calculator proxy = (Calculator) Proxy.newProxyInstance(calculator.getClass().getClassLoader(),
                calculator.getClass().getInterfaces(),
                logHandler);
        proxy.add(1,1);

    }

}

class LogHandler implements InvocationHandler {

    Object object;

    public LogHandler(Object object) {
        this.object = object;
    }

    public Object invoke(Object obj1, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        this.doBefore();
        Object o = method.invoke(obj1, args);
        this.doAfter();
        return o;
    }


    public void doBefore() {
        System.out.println("do this before!");
    }

    public void doAfter(){
        System.out.println("do this after!");
    }

}