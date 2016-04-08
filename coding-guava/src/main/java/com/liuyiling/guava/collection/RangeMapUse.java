package com.liuyiling.guava.collection;

import com.google.common.collect.Range;

/**
 * Created by liuyl on 16/4/7.
 * range最大的好处是可以用来处理区间排序：
 * 举例:曾经有这样的一个需求，一个视频直播分为IM流和视频流，我们需要统计IM流存在但是视频流不存在的情况
 * <p>
 * Guava Range 概念，范围和方法
 * 概念	表示范围	guava对应功能方法
 * (a..b)	{x | a < x < b}	open(C, C)
 * [a..b]	{x | a <= x <= b} 	closed(C, C)
 * [a..b)	{x | a <= x < b}	closedOpen(C, C)
 * (a..b]	{x | a < x <= b}	openClosed(C, C)
 * (a..+∞)	{x | x > a}	greaterThan(C)
 * [a..+∞)	{x | x >= a}	atLeast(C)
 * (-∞..b)	{x | x < b}	lessThan(C)
 * (-∞..b]	{x | x <= b}	atMost(C)
 * (-∞..+∞)	all values	all()
 */
public class RangeMapUse {

    public static void main(String[] agrs) {

        /**
         * contains判断值是否在当前range内
         */
        System.out.println(Range.closed(1, 3).contains(2));

        /**
         * 查询最低点
         */
        System.out.println(Range.closed(1, 3).lowerEndpoint());

        /**
         * 查询最高点
         */
        System.out.println(Range.closed(1, 3).upperEndpoint());

        /**
         *  比较range
         */
        System.out.println(Range.closed(1, 8).encloses(Range.closed(2,3)));

        /**
         * range是否可以连接
         */
        System.out.println(Range.closed(1, 8).isConnected(Range.closed(7,10)));

        /**
         * 返回交集
         */
        System.out.println(Range.closed(1, 8).intersection(Range.closed(7, 10)));

        /**
         * 返回并集
         */
        System.out.println(Range.closed(1, 8).span(Range.closed(7,10)));
    }

}
