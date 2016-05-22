package com.liuyiling.spring.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by liuyl on 16/5/7.
 */
public class TranscationRun {

    public static void main(String[] agrs){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");

        TranscationRun transactionRun = (TranscationRun) applicationContext.getBean("transactionRun");

        transactionRun.run();

    }


    UserDao userDao;

    public void run(){

        User user = new User();
        //重复主键
        user.setId(4);
        user.setName("tewo");
        userDao.insert(user);

        //insert抛异常，delete（1）回滚
        userDao.delete(4);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
