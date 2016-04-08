package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/12/11.
 */
public class InterfaceImplemention implements Interface {

    @Override
    public void fun() {
        int intA = Interface.INT_A;
        System.out.println(intA);
    }

    public static void main(String[] agrs){

        Interface interfaceImplemention = new InterfaceImplemention();
        interfaceImplemention.fun();
    }
}
