package com.liuyiling.coding.effective.common;

/**
 * Created by liuyl on 16/1/13.
 * 尽量使用基本类型,
 * 除非设计集合
 * 原因如下：
 */
public class PrimitiveAndObject {

    static  Integer i;

    public static void main(String[] agrs){
        /**
         * 陷进1:空指针引用
         */
        if( i == 42){
        }

        /**
         * 频繁的装箱拆箱影响性能
         */
        Long sum = 0L;
        for( int i = 0 ; i < Integer.MAX_VALUE; i++){
            sum = sum + 1;
        }
    }

}



