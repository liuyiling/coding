package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/12/10.
 * String类的api使用
 */
public class StringUser {

    public static void main(String[] agrs){
        String str  = "Would you like an apple?";
        System.out.println("String is : " + str);
        System.out.println("String length is : " + str.length());
        System.out.println("String chat is of 6 : " + str.charAt(6));
        System.out.println("String subString(10,15) : " + str.substring(10,15));
        System.out.println("String indexOf(apple) : " + str.indexOf("apple"));
        System.out.println("String upperCase is : " + str.toUpperCase());
        System.out.println("String lastIndexOf is : " + str.lastIndexOf("an"));
    }

}
