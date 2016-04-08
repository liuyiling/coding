package com.liuyiling.web;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * Created by liuyl on 15/11/29.
 */
public class MokitoSimpleTest {

    @Test
    public void simpleTest() {

        //第一种方式

        //创建mock对象
        List<String> list = mock(List.class);

        //设置预期返回值
        when(list.get(0)).thenReturn("test success!");

        //真实数据
        String result = list.get(0);

        //验证方法被调用过
        verify(list).get(0);

        Assert.assertEquals("test success!", result);


    }


    //设置顺序返回的参数
    @Test
    public void argumentMactherTest() {

        List<String> list = mock(List.class);

        when(list.get(anyInt())).thenReturn("get1", "get2");

        String result = list.get(0) + list.get(1);

        verify(list, times(2)).get(anyInt());

        Assert.assertEquals("get1get2", result);

    }

    /**
     * 验证方法是否被调用
     */
    @Test
    public void arguementMatchertest2(){

        Map<String,String> map = mock(Map.class);


        when(map.put(anyString(),anyString())).thenReturn("test");

        map.put("1","test");

        verify(map).put(eq("1"),eq("test"));


    }


}
