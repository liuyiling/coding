package com.liuyiling.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuyl on 16/3/30.
 */
public class NoUseTest {

    public static void main(String[] agrs){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");

        NoUse noUse = (NoUse) applicationContext.getBean("noUse");

        noUse.hello();

        Object o = applicationContext.getBean("son");

        if( o instanceof Father){
            System.out.println(true);
        }

    }
}

class Father{

}

class Son extends Father{

}

