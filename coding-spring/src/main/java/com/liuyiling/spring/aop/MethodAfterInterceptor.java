package com.liuyiling.spring.aop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * Created by liuyl on 16/5/7.
 */
public class MethodAfterInterceptor implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法" + method.getName() + "运行完毕，返回值=" + returnValue);
    }
}
