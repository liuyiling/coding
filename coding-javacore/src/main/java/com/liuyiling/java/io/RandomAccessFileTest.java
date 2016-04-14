package com.liuyiling.java.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by liuyl on 16/4/13.
 * io包中一直找不到类似于c语言中fopen的操作命令，今天发现了一个神器，记录下来，以备使用
 * RondomAccessFile内存映射文件解决了无法读取放入内存的大文件,该类生成一个内存映射文件，将fileChannel通道直接映射到内存中
 * 所以我们需要指定内存映射的位置，范围等参数
 */
public class RandomAccessFileTest {

    public static int length = 1 << 20;//1MB

    public static void main(String[] agrs) throws IOException {

        //开启一个读写通道
        FileChannel fileChannel = new RandomAccessFile("test.txt", "rw").getChannel();

        //对通道设置缓冲区
        MappedByteBuffer out = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);

        //写1MB的内容
        for( int i = 0; i < length; i++){
            out.put((byte) 'x');
        }

        System.out.println("finished");

        for( int k = length / 2; k < length/2 + 6; k++){
            System.out.println((char)out.get(k));
        }

        fileChannel.close();

    }

}
