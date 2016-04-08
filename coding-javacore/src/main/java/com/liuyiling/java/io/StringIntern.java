package com.liuyiling.java.io;

import java.util.Random;

/**
 * Created by liuyl on 16/1/22.
 */
public class StringIntern {

    static final int MAX = 1000 * 10000;
    static final String[] arr = new String[MAX];

    public static void main(String[] agrs){

        Integer[] DB_DATA = new Integer[10];
        Random random = new Random(10 * 10000);
        for(int i = 0; i < DB_DATA.length; i++){
            DB_DATA[i] = random.nextInt();
        }

        long t = System.currentTimeMillis();
        for(int i = 0; i < MAX; i++){
            arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length])).intern();
            //arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length]));
        }


        System.out.println((System.currentTimeMillis() - t) + "ms");
        System.gc();

    }

}
