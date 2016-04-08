package com.liuyiling.spring.ioc;

import java.io.PrintStream;
import java.util.List;

/**
 * Created by liuyl on 15/12/7.
 * 面向接口编程的第一个Spring实现
 */
public class IocBeanImpl implements IocBean {

    private int id;

    private List<String> list;

    public IocBeanImpl() {

        System.out.println("IocBeanImpl init");
    }

    public IocBeanImpl(int id) {
        this.id = id;
    }

    @Override
    public void sayHello() {
        System.out.println("This is my first Spring!");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
