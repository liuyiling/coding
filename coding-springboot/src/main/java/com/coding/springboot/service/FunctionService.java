package com.coding.springboot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static java.lang.System.out;

/**
 * Created by liuyiling on 17/7/18.
 */
@Service
@Scope("singleton")
public class FunctionService {

    @Value("another")
    private String another;

    public FunctionService() {
        out.println("constructor");
    }

    public void init(){
        out.println("init-method");
    }

    public void destroy(){
        out.println("destory-method");
    }

    public String getAnother() {
        return another;
    }

    public void setAnother(String another) {
        this.another = another;
    }

    public String sayHello(){
        return "Hello";
    }

}
