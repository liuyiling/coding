package com.liuyiling.java.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by liuyl on 15/12/16.
 * NIO 的IO方式
 */
public class NIOTest {


    public void selector() throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        Selector selector = Selector.open();//创建一个车辆调度系统
        ServerSocketChannel ssc = ServerSocketChannel.open();//创建一个高铁


        ssc.configureBlocking(false);//设置为非阻塞
        ssc.socket().bind(new InetSocketAddress(8089));//绑定高铁到对应线路

        ssc.register(selector, SelectionKey.OP_ACCEPT);//绑定高铁到对应调度系统

        while(true){
            Set selectKesy = selector.selectedKeys();//获取所有高铁信息
            Iterator it = selectKesy.iterator();
            while(it.hasNext()){
                SelectionKey key = (SelectionKey) it.next();
                if( (key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT){
                    ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssChannel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector, SelectionKey.OP_READ);
                    it.remove();
                } else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ){
                    SocketChannel sc = (SocketChannel) key.channel();
                    while(true){
                        buffer.clear();
                        int n = sc.read(buffer);
                        if(n <= 0){
                            break;
                        }
                        buffer.flip();
                    }
                    it.remove();
                }
            }


        }

    }


}
