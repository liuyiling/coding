package com.liuyiling.java.conrruent.doublecheck;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liuyl on 15/12/8.
 * java中的 double check
 */
public class UserCacheDBService {

    private volatile Map<Long, String> map = new ConcurrentHashMap<>();

    private Object mutex = new Object();

    /**
     * 先从cache取数据，没有的话再从db取数据
     */

    public String getString(long userId) {

        String string = map.get(userId);

        if (string == null) {//check
            synchronized (mutex) {//lock
                if ( !map.containsKey(userId) ) { //check
                    string = getFromDb();
                    map.put(userId,string);
                }
            }
        }

        if(string == null){
            string = map.get(userId);
        }


        return string ;
    }

    private String getFromDb() {
        return null;
    }

}