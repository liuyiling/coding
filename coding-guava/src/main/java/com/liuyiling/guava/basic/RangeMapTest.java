package com.liuyiling.guava.basic;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by liuyl on 15/12/3.
 * guava-cache的案例实现
 */
public class RangeMapTest {

    public static void main(String[] agrs){

        /**
         * 注意(10‥20)=ccc, [20‥20]=ccc 不能合并相邻区间，即使值都是ccc
         */
        RangeMap rangeMap = TreeRangeMap.create();
        rangeMap.put(Range.closed(1,10),"aaa");
        System.out.println(rangeMap);
        rangeMap.put(Range.open(3,6),"bbb");
        System.out.println(rangeMap);
        rangeMap.put(Range.openClosed(10, 20), "ccc");
        System.out.println(rangeMap);
        rangeMap.put(Range.closed(20,20),"ccc");
        System.out.println(rangeMap);
        rangeMap.remove(Range.closed(5, 11));
        System.out.println(rangeMap);


        iteratoRangeMap(rangeMap);


    }


    public static void iteratoRangeMap(RangeMap<Integer,String> intergerStringRangeMap){
        if(intergerStringRangeMap == null){
            return;
        }

        Map<Range<Integer>, String> rangeStringMap = intergerStringRangeMap.asMapOfRanges();
        Set<Map.Entry<Range<Integer>, String>> entries = rangeStringMap.entrySet();
        //获取key的集合
        //Set<Range<Integer>> ranges = rangeStringMap.keySet();
        Iterator<Map.Entry<Range<Integer>, String>> iterator = entries.iterator();
        while(iterator.hasNext()){
            Map.Entry<Range<Integer>, String> next = iterator.next();
            System.out.println(next.getKey() + "    " + next.getValue());
        }

    }
}
