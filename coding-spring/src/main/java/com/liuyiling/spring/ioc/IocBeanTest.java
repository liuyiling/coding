package com.liuyiling.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuyl on 15/12/7.
 */
public class IocBeanTest {

    public static void main(String[] agrs){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");

        //面向接口编程
        IocBean iocBean = (IocBean) applicationContext.getBean("iocBean");
        iocBean.sayHello();


        //这样就不能使用面向接口了,是的，所以要注意接口完整性地定义，
        //像下面这样写，代码的耦合性就比较大了
        IocBeanImpl firstBean1 = (IocBeanImpl) applicationContext.getBean("iocBean2");
        System.out.println(firstBean1.getId());
        System.out.println(firstBean1.getList());

        IocBean iocBean3 = (IocBean) applicationContext.getBean("iocBean3");
        iocBean3.sayHello();


    }
}
