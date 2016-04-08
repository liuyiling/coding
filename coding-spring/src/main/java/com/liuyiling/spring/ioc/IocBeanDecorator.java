package com.liuyiling.spring.ioc;

/**
 * Created by liuyl on 15/12/7.
 */
public class IocBeanDecorator implements IocBean{

    private IocBean iocBean;

    public IocBeanDecorator() {
        System.out.println("IocBeanDecorator init");
    }

    @Override
    public void sayHello() {
        System.out.println("decorator begin");
        iocBean.sayHello();
        System.out.println("decorator end");
    }

    public IocBean getIocBean() {
        return iocBean;
    }

    public void setIocBean(IocBean iocBean) {
        this.iocBean = iocBean;
    }
}
