package com.liuyiling.spring.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * Created by liuyl on 15/12/8.
 */
public class JdbcTest {

    public static void main(String[] agrs) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("coding-spring.xml");

        User user = (User) applicationContext.getBean("user");

        //使用callback函数进行结果集的处理
        List<User> list = user.findAllUser();
        System.out.println(list);


        //测试execute方法
        UserDao userDao = (UserDao) applicationContext.getBean("userDao");
        //userDao.testExecute();

        //测试执行update方法
        //userDao.tesetUpdate();


        userDao.insert(new User(2,"two"));
        //userDao.update(new User(2,"twotwo"));

        userDao.delete(2);
        //System.out.println(userDao.selectAll());
        //
        //System.out.println(userDao.selectById(1));


    }
}
