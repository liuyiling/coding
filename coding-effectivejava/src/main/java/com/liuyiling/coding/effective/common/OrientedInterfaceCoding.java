package com.liuyiling.coding.effective.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyl on 16/1/13.
 * 尽量使用面向接口编程
 */
public class OrientedInterfaceCoding {

    public static void main(String[] agrs){
        /**
         * 使用接口声明引用，如果要更换实现，只需要把ArrayList换成LinkedList
         */
        List<String> list = new ArrayList<>();
    }
}
