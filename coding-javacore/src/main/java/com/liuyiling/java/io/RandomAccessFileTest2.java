package com.liuyiling.java.io;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by liuyl on 16/4/13.
 * RandomAccessFile提供了类似于C语言中对文本的指针操作，虽然增加了程序员对文件的可控性，但是降低了读写的速率：
 * 因为RandomAccessFile的源码中没有使用缓冲区来进行读写处理
 */
public class RandomAccessFileTest2 {

    public static void main(String[] agrs) throws IOException {

        RandomAccessFile file = new RandomAccessFile("file","rw");

        //向file文件写数据
        file.writeInt(20);
        file.writeUTF("这是一个字符串");
        file.writeBoolean(true);
        file.writeShort(395);
        file.writeLong(2325451l);

        //指针设置到文件开始未知
        file.seek(0);

        System.out.println(file.readInt());
        System.out.println(file.readUTF());

        //跳过3个字节
        file.skipBytes(3);
        System.out.println(file.readLong());


        RandomAccessFile filecopy = new RandomAccessFile("fileCopy", "rw");
        file.seek(0);
        int len = (int) file.length();
        byte[] buffer = new byte[len];
        file.readFully(buffer);
        filecopy.write(buffer);
        System.out.println("complished");

    }

    public static void writeStr(long skip, String str, String fileName) throws IOException {

        RandomAccessFile file = new RandomAccessFile("file","rw");

        if( skip < 0 || skip > file.length()){
            System.out.println("跳过的字节数无效");
            return;
        }

        byte[] bytes = str.getBytes();
        file.setLength(file.length() + bytes.length);

        for( long i = file.length() - 1; i > bytes.length + skip - 1; i--){
            file.seek(i - bytes.length);
            byte temp = file.readByte();
            file.seek(i);
            file.writeByte(temp);
        }

        file.seek(skip);
        file.write(bytes);
        file.close();
    }
}
