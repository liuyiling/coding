package com.liuyiling.java.basic;

import java.util.Random;

/**
 * Created by liuyl on 15/11/24.
 */
public class BasicDataStructAndFlow {

    //数组声明的三种方式
    String[] array1 = new String[3];
    String[] array2 = new String[]{"a", "b", "c"};
    String[] array3 = {"a", "b", "c"};

    //多维数组
    String[][] array4 = new String[3][];

    public static void main(String[] agrs){

        Random random = new Random();
        int result = random.nextInt(4);

        //switch语句的使用：在每个case的后面都要加入break
        switch (result){
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
            case 3:
                System.out.println("3");
                break;
            default:
                System.out.println("else");
                break;
        }


    }

}
