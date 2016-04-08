package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/24.
 */
public class SequenceInitialize {
    public static void main(String[] agrs){
        Son son = new Son();
    }
}


class Father{
    public static int a = 10;
    public int b = 20;

    static{
        System.out.println("Father static code");
    }

    public Father() {
        System.out.println("Father Constructor");
    }

    {
        System.out.println("Father memeber");
    }
}


class Son extends Father{
    public static int a = 10;
    public int b = 20;

    static{
        System.out.println("Son static code");
    }

    public Son() {
        System.out.println("Son Constructor");
    }

    {
        System.out.println("Son memeber");
    }
}