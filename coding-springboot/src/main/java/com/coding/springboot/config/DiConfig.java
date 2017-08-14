package com.coding.springboot.config;

import com.coding.springboot.service.FunctionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuyiling on 17/7/18.
 */
@Configuration
@ComponentScan("com.coding.springboot")
public class DiConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    FunctionService functionService(){
        return new FunctionService();
    }

}
