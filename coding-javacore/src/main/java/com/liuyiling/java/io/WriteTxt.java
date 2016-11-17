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

        String[] firstName = new String[]{"w", "l", "z", "y", "h", "x", "s", "g"};
        String[] lastName = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q","r","s","t","u","v","w","x","y","z"};
        String[] lastName2 = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                "q","r","s","t","u","v","w","x","y","z"};

        try {
            File file = new File("/Users/meitu/Downloads/mailAddrs.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            String data = "";
            for(String one : firstName){
                for(String two : lastName){
                    for(String third : lastName2){
                        String totalName = one + two + third + "@meitu.com" + "\n";
                        data += totalName;
                    }
                }
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
