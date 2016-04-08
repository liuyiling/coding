package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/12/15.
 * 枚举类型的使用举例
 */
public enum EnumTest {

    ENUM_1(1,1),
    ENUM_2(2,2);


    private int value;
    private int minute;

    EnumTest(int value, int minute) {
        this.value = value;
        this.minute = minute;
    }

    public int value() {
        return value;
    }

    public int minute() {
        return minute;
    }

    public static EnumTest valueOf(int value) {
        for(EnumTest t : EnumTest.values()) {
            if (value == t.value()) {
                return t;
            }
        }
        return null;
    }

}
