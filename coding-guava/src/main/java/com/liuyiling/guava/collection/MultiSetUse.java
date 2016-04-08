package com.liuyiling.guava.collection;

import com.google.common.collect.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuyl on 16/4/6.
 * MultiSetUse的使用案例:
 * 　     add(E element) :向其中添加单个元素
 * 　　　　add(E element,int occurrences) : 向其中添加指定个数的元素
 * 　　　　count(Object element) : 返回给定参数元素的个数
 * 　　　　remove(E element) : 移除一个元素，其count值 会响应减少
 * 　　　　remove(E element,int occurrences): 移除相应个数的元素
 * 　　　　elementSet() : 将不同的元素放入一个Set中
 * 　　　　entrySet(): 类似与Map.entrySet 返回Set<Multiset.Entry>。包含的Entry支持使用getElement()和getCount()
 * 　　　　setCount(E element ,int count): 设定某一个元素的重复次数
 * 　　　　setCount(E element,int oldCount,int newCount): 将符合原有重复个数的元素修改为新的重复次数
 * 　　　　retainAll(Collection c) : 保留出现在给定集合参数的所有的元素
 * 　　　　removeAll(Collectionc) : 去除出现给给定集合参数的所有的元素
 */
public class MultiSetUse {


    public static void main(String[] agrs) {

        String[] words = new String[]{"a", "b", "a", "d", "e"};
        Map<String, Integer> counts = new HashMap<>();
        for (String word : words) {
            Integer count = counts.get(word);
            if (count == null) {
                counts.put(word, 1);
            } else {
                counts.put(word, count + 1);
            }
        }
        //以上是传统的统计信息的方式,不是特别的好用

        //上面的代码实现的功能非常简单，用于记录字符串在数组中出现的次数。这种场景在实际的开发过程还是容易经常出现的，如果使用实现Multiset接口的具体类就可以很容易实现以上的功能需求
        List<String> wordList = new ArrayList<>();
        for (String word : words) {
            wordList.add(word);
        }
        Multiset<String> countsSet = HashMultiset.create();
        countsSet.addAll(wordList);

        for (String key : countsSet.elementSet()) {
            System.out.println(key + " count: " + countsSet.count(key));
        }
    }

}
