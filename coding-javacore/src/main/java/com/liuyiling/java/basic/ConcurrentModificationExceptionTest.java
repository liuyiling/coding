package com.liuyiling.java.basic;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by liuyl on 15/12/1.
 * 学习集合类api的正确使用
 */
public class ConcurrentModificationExceptionTest {


    public static void main(String[] agrs){

        User user1 = new User();
        user1.setId(1);
        user1.setName("one");

        User user2 = new User();
        user2.setId(2);
        user2.setName("two");

        //其他的集合类也一样
        Set userSet = new HashSet<>();

        userSet.add(user1);
        userSet.add(user2);

        for(Iterator it = userSet.iterator(); it.hasNext();){
            User user = (User) it.next();
            if(user.getId() == 1){
                //下一行代码会出错，因为对set的操作不会更新iterator维护的内存索引表
                //userSet.remove(user);
                //解决方法1
                it.remove();
            }

            if(user.getId() == 2){
                user.setName("twotwo");
            }
        }

        //
        for(Iterator it = userSet.iterator(); it.hasNext();){
            User user = (User) it.next();
            System.out.println("user.getId() = " + user.getId());
        }



        //以下代码会出错
        //foreach是iterator的语法糖
        //List<String> list = new ArrayList();
        //list.add("a");
        //list.add("b");
        //for(String s : list){
        //    if ( s.equals("b")){
        //        list.remove(s);
        //    }
        //}

        List<String> list = new CopyOnWriteArrayList<>();
        list.add("a");
        list.add("b");

        for(String s : list){
            if ( s.equals("b")){
                list.remove(s);
            }
        }

    }





}

class User{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
