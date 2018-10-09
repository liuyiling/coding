package com.liuyiling.java.io;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.System.out;

/**
 * Created by liuyl on 15/12/2.
 * 读txt
 */
public class ReadTxt {

    public static Map<String, String> a250Lines = new HashMap<>();
    public static List<String> a200Lines = new ArrayList<>();

    public static void main(String[] agrs) throws IOException {
        read200lines();
        read250lines();
        print();
//        String result = changeTotalName("CHINA中国");
//        out.println(result);
    }

    private static void read200lines() throws IOException {
        String pathname = "/Users/liuyiling/Downloads/200lines.txt";
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while (line != null) {
            line = br.readLine();
            a200Lines.add(line);
        }
    }

    private static void read250lines() throws IOException {
        String pathname = "/Users/liuyiling/Downloads/250lines.txt";
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(
                new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while (line != null) {
            line = br.readLine();
            if (line != null) {
                if (line.split("\t").length == 18) {
                    String twoCode = line.split("\t")[2];
                    String threeCode = line.split("\t")[17];
                    a250Lines.put(twoCode, threeCode);
                }
            }
        }
    }

    private static void print() {
        FileWriter fw = null;
        try {
            fw = new FileWriter("/Users/liuyiling/Downloads/liuyiling.txt");
            for (String line : a200Lines) {
                boolean isFind = false;
                for (Map.Entry<String, String> entry : a250Lines.entrySet()) {
                    String twoCode = entry.getKey();
                    String threeCode = entry.getValue();
                    int threeCodeStartPoint = line.indexOf("=") + 2;
                    int threeCodeEndPoint = threeCodeStartPoint + 3;
                    if (StringUtils.equals(line.substring(threeCodeStartPoint, threeCodeEndPoint), threeCode)) {
                        isFind = true;
//                        String newLine = line.replaceFirst(threeCode, twoCode);
//                        fw.write(newLine + "\n");
//                        out.println(newLine);
                        String totalName = line.substring(line.indexOf(">") + 1, line.lastIndexOf("<"));
                        StringBuilder sb = new StringBuilder();
                        sb.append("case \'");
                        sb.append(twoCode);
                        sb.append("\'");
                        sb.append(": return ");
                        sb.append("\'");
                        String renderName = changeTotalName(totalName);
                        if(renderName.charAt(renderName.length() - 1) == '\u0000'){
                            renderName = renderName.substring(0, renderName.length() - 1);
                        }
                        sb.append(renderName);
                        sb.append("\',");
//                        fw.write(sb.toString() + "\n");
                        out.println(sb.toString());
                        break;
                    }
                }

                if (!isFind) {
//                    fw.write("=====" + line + "\n");
                    out.println("=====" + line);
                }
            }
            fw.close();
        } catch (Exception e) {
        } finally {
        }
    }

    private static String changeTotalName(String orgStr) {
        char[] orgStrArray = orgStr.toCharArray();
        int findIndex = 0;

        for (int i = 0; i < orgStrArray.length; i++) {
            if ((orgStrArray[i] >= 'a' && orgStrArray[i] <= 'z') ||
                    (orgStrArray[i] >= 'A' && orgStrArray[i] <= 'Z')) {
                findIndex = i;
            }
        }

        char[] dstStrArray = new char[orgStrArray.length + 1];
        if(orgStrArray[orgStrArray.length - 1] == ' '){
            dstStrArray = new char[orgStrArray.length];
        }

        int i = 0;
        for (char temp : orgStrArray) {
            if (i <= findIndex) {
                dstStrArray[i] = temp;
                i++;
                continue;
            }
            if (i == findIndex + 1) {
                dstStrArray[i] = ' ';
                i++;
//                continue;
            }
            if (i > findIndex) {
                if(temp != ' '){
                    dstStrArray[i] = temp;
                    i++;
                    continue;
                }

            }

        }

        return new String(dstStrArray);
    }
}
