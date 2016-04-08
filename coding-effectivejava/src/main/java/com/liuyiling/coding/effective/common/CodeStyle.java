package com.liuyiling.coding.effective.common;

import java.util.*;

/**
 * Created by liuyl on 16/1/13.
 * 这个类中包含了一些编码的规范:
 */
public class CodeStyle {

    /**
     * 在第一次使用局部变量的时候,才进行初始化
     */
    public void fiedlInitialize(){
        /**
         * 这边有其余的代码
         */
        String name = "liuyiling";
        //最近使用name的地方
        System.out.println(name);
    }


    /**
     * 如果在循环之后不需要用到循环变量的内容，for优先于while
     * for也增加了可读性
     */
    public void forAndWhile(){

        List list = new ArrayList<>();

        for(Iterator it = list.iterator(); it.hasNext(); ){
            doSomeThing(it.next());
        }

    }

    public void doSomeThing(Object object){
    }

    /**
     * 多使用foreach循环而非for循环
     * 以下情况不能用foreach:
     *  1.遍历集合的时候需要删除元素
     */
    public void moreUseForeach(){

        Collection<Integer> list = Arrays.asList();
        /**
         * 这边的例子如果用普通的for循环就会出错
         */
        for( Integer i : list){
            for( Integer j : list){
                System.out.println(i + " " + j);
            }
        }
    }

    public static void main(String[] agrs){


        Map<Integer, String> map = new HashMap<>();
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");

        /**
         * 遍历的时候不能改变表的结构，以下代码是错误的
         */
        //for( Map.Entry<Integer, String> entry : map.entrySet()){
        //    if ( entry.getKey() == 2) {
        //        map.remove(entry.getKey());
        //    }
        //}


        /**
         * 必须用这种方式:使用iterator来进行删除
         */
        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();
        while ( iterator.hasNext() ){
            Map.Entry<Integer, String> next = iterator.next();
            iterator.remove();
        }
        System.out.println(map.toString());

    }
}

enum Face {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    private Integer value;

    Face(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    public static Face valueOf(Integer value) {
        for(Face t : Face.values()) {
            if (value == t.value()) {
                return t;
            }
        }
        return null;
    }

}