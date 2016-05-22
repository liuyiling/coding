package com.liuyiling.spring.aop;

import com.liuyiling.spring.ioc.IocBeanImpl;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * Created by liuyl on 16/2/20.
 */
public class MethodBeforeAdviceImpl implements MethodBeforeAdvice {


    //参数分别为，被调用的方法，被调用的方法参数，对象
    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("运行前检查...");
        System.out.println("Method: " + method.getName());

        if( o instanceof IocBeanImpl){
            int id = ((IocBeanImpl) o).getId();

            if( id == 0){
                throw new NullPointerException("name属性不能为null");
            }
        }
    }
}
