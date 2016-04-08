package com.liuyiling.coding.effective.common;

import java.util.Random;

/**
 * Created by liuyl on 16/1/13.
 * 使用基础类库：
 * 1.站在巨人的肩膀上
 * 2.更多时间专注于业务逻辑而非底层细节
 * 3.标准类库的性能往往随着时间逐步提高，无需我们自己进行优化
 * 4.这是一种工业标准，增加可读性，全球化阅读
 *
 * 综上所诉：
 *  保持好奇心，多花时间研究(重点)标准类库以及它们的新特性
 *  不要重复发明轮子
 */
public class UseBasicClass {

    public static void main(String[] agrs){
        Random random = new Random();
        int i = random.nextInt(100);
        System.out.println(i);
    }

}
