package com.liuyiling.coding.effective.common;

/**
 * Created by liuyl on 16/1/13.
 * 除非不在意性能，不然就用StringBuilder代替String
 */
public class StringBuilder {

    public static void main(String[] agrs){
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(true);
        sb.append(29);
        System.out.println(sb.toString());

    }

}
