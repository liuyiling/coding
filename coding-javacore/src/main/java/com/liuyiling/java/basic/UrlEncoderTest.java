package com.liuyiling.java.basic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liuyl on 15/11/25.
 * UrlEncode的使用方向：
 *
 */
public class UrlEncoderTest {

    public static void main(String[] agrs){

        try {
            System.out.println(URLEncoder.encode("可家好帅","UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
