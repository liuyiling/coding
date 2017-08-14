package com.coding.springboot.config;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static java.lang.System.out;

/**
 * Created by liuyiling on 17/7/18.
 */
@Configuration
@ComponentScan("com.coding.springboot")
@PropertySource("classpath:test.properties")
public class ElConfig {

    @Value("i love you")
    private String normal;

    @Value("#{systemProperties['os.name']}")
    private String osName;

    @Value("#{ T(java.lang.Math).random() * 100}")
    private String randomNumer;

    @Value("#{functionService.another}")
    private String fromAnother;

    @Value("classpath:test.properties")
    private Resource testFile;

    @Value("${book.name}")
    private String bookName;

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource(){
        out.println(normal);
        out.println(osName);
        out.println(randomNumer);
        out.println(fromAnother);
        out.println(bookName);
        try {
            out.println(IOUtils.toString(testFile.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
