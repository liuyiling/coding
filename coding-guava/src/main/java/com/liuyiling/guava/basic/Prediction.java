package com.liuyiling.guava.basic;

import com.google.common.base.Preconditions;

/**
 * Created by liuyl on 16/1/14.
 * 前置条件:
 * 用于方法调用（包括set方法）时
 * checkArgument(boolean)	检查boolean是否为true，用来检查传递给方法的参数。	IllegalArgumentException
 * checkNotNull(T)	检查value是否为null，该方法直接返回value，因此可以内嵌使用checkNotNull。	NullPointerException
 * checkState(boolean)	用来检查对象的某些状态。	IllegalStateException
 * checkElementIndex(int index, int size)	检查index作为索引值对某个列表、字符串或数组是否有效。index>=0 && index<size *	IndexOutOfBoundsException
 * checkPositionIndex(int index, int size)	检查index作为位置值对某个列表、字符串或数组是否有效。index>=0 && index<=size *	IndexOutOfBoundsException
 * checkPositionIndexes(int start, int end, int size)	检查[start, end]表示的位置范围对某个列表、字符串或数组是否有效*	IndexOutOfBoundsException
 * <p></p>
 * *索引值常用来查找列表、字符串或数组中的元素，如List.get(int), String.charAt(int)
 * 位置值和位置范围常用来截取列表、字符串或数组，如List.subList(int，int), String.substring(int)
 */
public class Prediction {

    public static void main(String[] agrs) {
        Car car = new Car();
        car.setOilUse(-1);
    }
}


class Car {

    private int oilUse;

    private String name;

    public void setOilUse(int oilUse) {
        Preconditions.checkArgument(oilUse >= 0, " oilUse must be bigger than 0", oilUse);
        this.oilUse = oilUse;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        /**
         * 使用toStringHelper进行toString方法的简单构造
         */
        return com.google.common.base.Objects.toStringHelper(this).add("x", true).toString();
    }
}