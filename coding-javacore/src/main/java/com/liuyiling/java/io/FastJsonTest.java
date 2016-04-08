package com.liuyiling.java.io;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by liuyl on 15/12/24.
 */
public class FastJsonTest {


    public static void main(String[] agrs) {

        //在json中加入jsonArray
        //JSONArray jsonArray = new JSONArray();
        //jsonArray.add(1);
        //jsonArray.add(2);
        //jsonArray.add(3);
        //
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("liveId", "2");
        //jsonObject.put("forbidUser", jsonArray);
        //
        //System.out.println(jsonObject.toJSONString());

        //Set转换为jsonArray
        //Set<Long> set = new HashSet<>();
        //set.add((long) 1);
        //set.add((long) 2);
        //JSONArray jsonArray = JSONArray.parseArray(set.toString());
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("forbidUser", jsonArray);
        //System.out.println(jsonObject.toJSONString());


        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("zh-Hans", "圣诞快乐");
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("zh-Hans", "圣诞快乐");
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("zh-Hans", "圣诞快乐");
        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("zh-Hans", "圣诞快乐");
        jsonArray.add(jsonObject1);
        jsonArray.add(jsonObject2);
        jsonArray.add(jsonObject3);
        jsonArray.add(jsonObject4);
        System.out.println(jsonArray.toString());
        jsonObject.put("content", jsonArray);
        System.out.println(jsonObject.toString());

        //JSONObject jsonObject2 = new JSONObject();
        //JSONArray jsonArray2 = new JSONArray();
        //jsonObject2.put("2","2");
        //jsonArray2.add(jsonObject2);
        //System.out.println(jsonArray2.toString());
        //JSONObject jsonObject = new JSONObject();
        //jsonObject.put("key", jsonArray2);
        //System.out.println(jsonObject.toString());


    }

    public static void speedCompare(){

        long begin = System.currentTimeMillis();


        long end = System.currentTimeMillis();

    }


}
