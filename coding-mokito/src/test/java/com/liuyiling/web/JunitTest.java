package com.liuyiling.web;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by liuyl on 15/12/1.
 * Junit的使用教程
 */
public class JunitTest {

    /**
     * 判断两个数组相等
     */
    @Test
    public void testArray(){

        List<String> actual = Arrays.asList("a","b","c");
        List<String> expected = Arrays.asList("a","b","c");

        Assert.assertThat(actual, is(expected));
    }

    @Test
    public void testEqual(){

        Assert.assertTrue(1==1);
    }

}
