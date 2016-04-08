package com.liuyiling.spring.dependson;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuyl on 15/12/8.
 */
public class DependsOnTest {


    public static void main(String[] agrs){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");
        DependentBean dependentBean = (DependentBean) applicationContext.getBean("dependentBean");

        /**
         * 一定要有这一句话，不然destory-method不会执行
         */
        applicationContext.registerShutdownHook();
        dependentBean.write("hehe");
    }
}
