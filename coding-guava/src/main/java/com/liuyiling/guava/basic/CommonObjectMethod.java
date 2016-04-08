package com.liuyiling.guava.basic;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * Created by liuyl on 16/1/14.
 * guava中提供了常见的Objects方法
 */
public class CommonObjectMethod {

    private int num;
    private int day;

    public static void main(String[] agrs) {

        /**
         * 覆写equals方法的时候，当一个对象的字段可以为null时，判断会比较痛苦，因为要对字段执行null敏感的判断
         * 使用Objects.equal方法可以帮你判断null的值
         */
        Objects.equal(null, null);

        CommonObjectMethod c1 = new CommonObjectMethod();

        c1.num = 1;
        c1.day = 1;

        CommonObjectMethod c2 = new CommonObjectMethod();

        c2.num = 2;
        c2.day = 2;

        System.out.println(c1.compareTo(c2));

        CommonObjectMethod commonObjectMethod = new CommonObjectMethod();
        commonObjectMethod.setDay(1);
        commonObjectMethod.setNum(2);
        System.out.println(commonObjectMethod);


    }


    /**
     * 很适合在整个工程中都重写这个方法
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(day,num);
    }

    /**
     * 简洁明了的compareTo方法
     */

    public int compareTo(CommonObjectMethod that) {

        return ComparisonChain.start()
                .compare(this.num, that.num)
                .compare(this.day, that.day)
                .result();

    }


    @Override
    public String toString() {
        /**
         * 使用toStringHelper进行toString方法的简单构造
         */
        return com.google.common.base.Objects.toStringHelper(this).add("day", day).add("num", num).toString();
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
