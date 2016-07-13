package com.liuyiling.java.network;

import java.util.Hashtable;

/**
 * Created by liuyl on 16/6/27.
 */
public class TomcatStringManager {
}

class StringManager{

    private StringManager stringManager = null;

    public StringManager getSingleton(){

        if(stringManager == null){
            stringManager = new StringManager();
        }

        return stringManager;
    }

}

class StringManager2{

    private static Hashtable managers = new Hashtable();
    private String packName;

    public synchronized static StringManager2 getStringManager(String packName){

        StringManager2 mgr = (StringManager2) managers.get(packName);
        if(mgr == null){
            mgr = new StringManager2(packName);
            managers.put(packName, mgr);
        }
        return mgr;
    }

    public StringManager2(String packName) {
        this.packName = packName;
    }

}