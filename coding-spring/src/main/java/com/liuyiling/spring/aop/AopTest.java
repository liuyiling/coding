package com.liuyiling.spring.aop;

import com.liuyiling.spring.ioc.IocBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * Created by liuyl on 16/2/20.
 * 面向aop编程
 */
public class AopTest {


    public static void main(String[] agrs){

        System.out.println(new Date());

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");
        IocBean iocBean = (IocBean) applicationContext.getBean("iocBeanAop");

        iocBean.sayHello();
    }


}
