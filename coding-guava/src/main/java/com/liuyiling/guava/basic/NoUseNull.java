package com.liuyiling.guava.basic;

import com.google.common.base.Optional;

/**
 * Created by liuyl on 16/1/14.
 * 尽量不使用空值null，因为：
 *  map.get(key) == null 为true时
 *  可能是不存在对应的entry或者是key存在但是对应的value为null
 *
 * 使用方式：
 *  假设某一个方法返回某个值，代码依据返回值做下一步判断，可以区分以下三中情况:不存在，存在，存在且为null
 *
 */
public class NoUseNull {

    public static void main(String[] agrs){

        /**
         * 创建指定引用的Optional实例，若引用为null则快速失败
         */
        Optional<Integer> possible = Optional.of(6);
        if(possible.isPresent()){
            System.out.println(possible.isPresent());
            System.out.println(possible.get());
        }

        /**
         * 创建引用缺失的Optional实例
         */
        Optional<Integer> absent = Optional.absent();
        if(absent.isPresent()){
            System.out.println(absent.isPresent());
        }

        /**
         * 创建指定引用的Optional实例，若引用为null则表示缺失
         */
        Optional<Integer> nullAble = Optional.fromNullable(null);
        if(nullAble.isPresent()){
            System.out.println(nullAble.isPresent());
        }

        /**
         * 创建指定引用的Optional实例，若引用为null则表示缺失
         */
        Optional<Integer> noNullAble = Optional.fromNullable(10);
        if(noNullAble.isPresent()){
           System.out.println(noNullAble.isPresent());
        }

    }

}
