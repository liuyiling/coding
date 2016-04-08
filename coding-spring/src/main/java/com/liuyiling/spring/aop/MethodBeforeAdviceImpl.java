package com.liuyiling.spring.aop;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by liuyl on 16/2/20.
 */
public class MethodBeforeAdviceImpl implements MethodBeforeAdvice {


    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("运行前检查...");
        System.out.println("Method: " + method.getName());
    }
}
