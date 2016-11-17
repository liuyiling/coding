package com.liuyiling.java.jdk8;


import java.util.Optional;

/**
 * Created by liuyl on 16/8/16.
 */
public class OptionalTest {

    public static void main(String[] agrs){

        Optional<String> notNull = Optional.of("not null");
        System.out.println("notNull is set?" + notNull.isPresent());

        Optional<String> fullName = Optional.ofNullable( null );
        System.out.println("Full Name is set?" + fullName.isPresent());

    }
}
