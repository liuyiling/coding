package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/24.
 */
public class ClassAndObject {

    static int a;

    static {
        System.out.println("静态代码块，只执行一次");
    }

    {
        System.out.println("类的初始化块，每新建一个类都会运行这一段代码");
    }

    public static void main(String[] agrs) {

        //局部变量需要初始化
        int b = 0;
        System.out.println(b);
        //静态变量或者成员变量会自动初始化
        System.out.println(a);

        //静态代码块，只会执行一次
        ClassAndObject classAndObject = new ClassAndObject();
        ClassAndObject classAndObject2 = new ClassAndObject();


    }

}
