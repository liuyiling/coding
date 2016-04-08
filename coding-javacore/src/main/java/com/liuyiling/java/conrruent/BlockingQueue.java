package com.liuyiling.java.conrruent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liuyl on 16/3/2.
 * 阻塞队列的研究
 */
public class BlockingQueue {

    private List queue = new LinkedList<>();

    private int limit = 10;

    public BlockingQueue(int limit) {
        this.limit = limit;
    }

    public synchronized void enqueue(Object item) throws InterruptedException {
        while(this.queue.size() == this.limit){
            wait();
        }
        if(this.queue.size() == 0){
            notifyAll();
        }
        this.queue.add(item);
    }

    public synchronized Object dequeue() throws InterruptedException {
        while(this.queue.size() == 0){
            wait();
        }
        if( this.queue.size() == this.limit){
            notifyAll();
        }

        return this.queue.remove(0);
    }

}
