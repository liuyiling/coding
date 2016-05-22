package com.liuyiling.java.collection;

import java.util.*;

/**
 * Created by liuyl on 16/5/9.
 */
public class CollectionUse {

     public static void main(String[] agrs){

         ArrayList array = new ArrayList<>();
         //O(1)时间复杂度
         array.set(0, "a");
         array.get(0);
         array.isEmpty();
         array.size();
         array.ensureCapacity(100);

         //可以用来当做队列或者堆栈
         LinkedList list = new LinkedList<>();
         list.get(0);
         list.set(0, "1");
         list.remove(0);
         list.addFirst("1");
         list.removeLast();


         //任何作为key的Object都要override两个方法：hashCode和equals
         Map hashMap = new HashMap<>();
         hashMap.put("one", new Integer(1));


         //任何集合都实现了iterator方法
         Iterator iterator = list.iterator();
         while(iterator.hasNext()){
             Object next = iterator.next();
             iterator.remove();
         }


         //部分集合实现了linkedIterator
         ListIterator listIterator = list.listIterator(2);
         listIterator.previous();
         listIterator.previousIndex();


     }
}
