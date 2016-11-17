package com.liuyiling.java.jdk8;

import java.util.Arrays;

/**
 * Created by liuyl on 16/8/16.
 */
public class Lambda {

    public static void main(String[] agrs) {
        Arrays.asList("a", "b", "c").forEach(e->System.out.println(e));

        Arrays.asList("a", "b", "c").sort((e1, e2) -> e1.compareTo(e2));

    }

}
