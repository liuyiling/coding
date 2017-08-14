package com.coding.springboot.test;

import com.coding.springboot.config.DiConfig;
import com.coding.springboot.config.ElConfig;
import com.coding.springboot.service.FunctionService;
import com.coding.springboot.service.UseFunctionService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static java.lang.System.out;

/**
 * Created by liuyiling on 17/7/18.
 */
public class UseExample {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DiConfig.class);

        UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
        UseFunctionService useFunctionService2 = context.getBean(UseFunctionService.class);

        FunctionService functionService = context.getBean(FunctionService.class);
        FunctionService functionService2 = context.getBean(FunctionService.class);

        out.println(useFunctionService.sayHello());
        out.println("useFunctionService与useFunctionService2是否相等:" + (useFunctionService == useFunctionService2));
        out.println("functionService与functionService2是否相等:" + (functionService == functionService2));
        context.close();


        AnnotationConfigApplicationContext eiContext = new AnnotationConfigApplicationContext(ElConfig.class);
        ElConfig elConfig = eiContext.getBean(ElConfig.class);
        elConfig.outputResource();
    }

}
