package com.liuyiling.java.cglib;

import com.liuyiling.java.staticProxy.RealSubject;
import com.liuyiling.java.staticProxy.Subject;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Created by liuyl on 2016/11/20.
 */
public class CGlibProxyTest {

    public static void main(String[] agrs){
        Subject proxy = CGlibProxy.getInstance().getProxy(RealSubject.class);
        proxy.dealTask("dealTask");
    }

}

class CGlibProxy implements MethodInterceptor{

    private static CGlibProxy instance = new CGlibProxy();

    private CGlibProxy(){

    }

    public static CGlibProxy getInstance(){
        return instance;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        befor();
        Object result = methodProxy.invokeSuper(o, objects);
        after();
        return result;
    }

    public <T> T getProxy(Class<T> cls){
        return (T)Enhancer.create(cls, this);
    }

    public void befor(){
        System.out.println("CGlibProxy 开始");
    }

    public void after(){
        System.out.println("CGlibProxy 结束");
    }
}
