package com.liuyiling.coding.effective.common;

/**
 * Created by liuyl on 16/3/31.
 * 如何覆写equals有一定的方法和规范要遵守：
 * equals方法要遵循自反性，传递性，一致性
 * 并且覆盖了equals方法的也要覆盖hashCode方法--》理由是，你很有可能把这个对象放入HashSet,HashMap中
 */
public class HowToOverrideEquals {
}

class PhoneNumber implements Comparable{
    private final short areaCode;
    private final short prefix;
    private final short lineNumber;

    public PhoneNumber(short areaCode, short prefix, short lineNumber) {
        this.areaCode = areaCode;
        this.prefix = prefix;
        this.lineNumber = lineNumber;
    }


    @Override
    public boolean equals(Object obj) {
        if( obj == this ){
            return true;
        }
        if( !(obj instanceof PhoneNumber)){
            return false;
        }
        PhoneNumber phoneNumber = (PhoneNumber) obj;
        return phoneNumber.areaCode == this.areaCode
                && phoneNumber.prefix == this.prefix
                && phoneNumber.lineNumber == this.lineNumber;
    }

    /**
     * 我们生成的类有一大部分需要放在hash集合中，所以必须复写hashCode方法
     * @param obj
     * @return
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + areaCode;
        result = 31 * result + prefix;
        result = 31 * result + lineNumber;
        return result;
    }


    /**
     * 因为排序集合需要根据compare来进行排序，所以如果要将对象放进排序集合，也应该复写这个方法
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
