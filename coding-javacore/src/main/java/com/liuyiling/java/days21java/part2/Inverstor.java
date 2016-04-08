package com.liuyiling.java.days21java.part2;

/**
 * Created by liuyl on 15/12/10.
 */
public class Inverstor {

    public static void main(String[] agrs){

        float total = 14000f;

        total = total + ( total * 0.4f);

        System.out.println("1: " + total);

        total = total - 1500;

        System.out.println("2: " + total);

        total = total + (total * 0.12f);

        System.out.println("3: " + total);

    }
}
