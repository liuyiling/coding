package com.liuyiling.java.io;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by liuyl on 16/2/9.
 * 字节到字符的编解码
 */
public class StreamToCharacter {

    public static void main(String[] agrs){

        StringBuffer str = new StringBuffer();
        char[] buf = new char[1024];
        FileReader f = null;
        try {
            f = new FileReader("file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            while(f.read(buf) > 0){
                str.append(buf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(str.toString());

    }



}
