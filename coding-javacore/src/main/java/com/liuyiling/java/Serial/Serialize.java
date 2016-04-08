package com.liuyiling.java.Serial;

import java.io.*;

/**
 * Created by liuyl on 15/12/16.
 * java 序列化和反序列化
 */
public class Serialize implements Serializable {


    private static final long serialVersionUID = -6849_4949_4949_4949_49L;

    private int num = 610;

    public static void main(String[] agrs) throws IOException {

        FileOutputStream fileOutputStream = new FileOutputStream("/Users/a/Documents/a.com/test.dat");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        Serialize serialize = new Serialize();

        objectOutputStream.writeObject(serialize);

        objectOutputStream.flush();

        objectOutputStream.close();

    }

}
