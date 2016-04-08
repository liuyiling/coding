package com.liuyiling.java.io;

import java.io.*;

/**
 * Created by liuyl on 15/12/2.
 * 读txt
 */
public class ReadTxt {

    public static void main(String[] agrs) throws IOException {

        File file = new File("/Users/a/Documents/a.com/fakeUids.txt");

        String encoding = "UTF-8";
        if (file.isFile() && file.exists()) {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt = null;
            while( (lineTxt = bufferedReader.readLine()) != null){
                System.out.println(lineTxt);
            }
        } else {
            System.out.println("not found file");
        }

    }

}
