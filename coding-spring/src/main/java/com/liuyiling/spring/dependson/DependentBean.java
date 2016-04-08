package com.liuyiling.spring.dependson;

import java.io.IOException;

/**
 * Created by liuyl on 15/12/8.
 */
public class DependentBean {

    ResourceBean resourceBean;

    public void write(String ss){
        System.out.println("DependentBean: write");
        try {
            resourceBean.getFos().write(ss.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    初始化方法
    public void init(){
        System.out.println("DependentBean: init");
        try {
            resourceBean.getFos().write("DependentBean: init".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //销毁方法
    public void destory(){
        System.out.println("DependentBean: destory");
        try {
            resourceBean.getFos().write("DependentBean: destory".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setResourceBean(ResourceBean resourceBean) {
        this.resourceBean = resourceBean;
    }
}
