package com.liuyiling.java.collection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyl on 15/12/3.
 * map循环遍历的方式
 */
public class MapIterator {

    public static void main(String[] agrs){

        Map<Integer,String> map = new HashMap<>();

        map.put(1,"a");
        map.put(2,"b");

        for(Map.Entry<Integer,String> entry : map.entrySet()){
            System.out.println("key= " + entry.getKey() + "  value = "  + entry.getValue());
        }
    }
}
