package com.liuyiling.common.codestyle;

import java.util.*;

import static java.lang.System.out;

/**
 * Created by liuyiling on 17/7/7.
 */
public class CollectionStyle {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        for(Map.Entry<String, String> entry : map.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
        }

    }

}
