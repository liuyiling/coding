package com.liuyiling.java.standard;

/**
 * Created by liuyiling on 17/5/24.
 * 枚举类型最好加上ENUM作为后缀
 */
public enum  DemoEnum {

    ENUM_1(1,1),
    ENUM_2(2,2);

    private int value;
    private int minute;

    DemoEnum(int value, int minute) {
        this.value = value;
        this.minute = minute;
    }

    public int value() {
        return value;
    }

    public int minute() {
        return minute;
    }

    public static DemoEnum valueOf(int value) {
        for(DemoEnum t : DemoEnum.values()) {
            if (value == t.value()) {
                return t;
            }
        }
        return null;
    }

}
