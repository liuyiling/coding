package com.liuyiling.java.basic;

import java.util.Random;

/**
 * Created by liuyl on 15/12/2.
 */
public class RandomTest {

    public static void main(String[] agrs){
        int max = 20;
        int min = 10;
        Random random = new Random();

        int s = random.nextInt(max) % (max-min+1) + min;
        System.out.println(s);
    }
}
