package com.coding.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * Created by liuyiling on 17/7/18.
 */
@Service
@Scope("prototype")
public class UseFunctionService {

    @Autowired
    FunctionService functionService;

    public String sayHello(){
        return functionService.sayHello();
    }
}
