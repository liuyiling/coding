package com.liuyiling.java.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by liuyl on 15/12/14.
 */
public class WriteTxt {


    public static void main(String[] agrs) {

        try {
            String data = "This conten will append to the end of the file";

            File file = new File("/Users/a/Downloads/test.txt");

            if (!file.exists()) {
                file.createNewFile();
            }


            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
