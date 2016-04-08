package com.liuyiling.java.conrruent;

/**
 * Created by liuyl on 16/3/2.
 * 几种锁的原理：
 * 1.自旋锁
 * 2.读写锁
 */
public class LockExample {
}

class Lock{

    private boolean isLocked = false;

    public synchronized void lock() throws InterruptedException {
        //因为一些奇怪的原因，wait可能不被notify就突然被唤醒，也称作假唤醒
        while (isLocked){
            wait();
        }
        isLocked = true;
    }

    public synchronized void unlock() {
        isLocked = false;
        notify();
    }

}

class ReadWriteLock{

    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    public synchronized void lockRead() throws InterruptedException {
        //没有线程拥有写锁+没有线程请求写锁
        while(writers > 0 || writeRequests > 0){
            wait();
        }
        readers++;
    }

    public synchronized void unlockRead(){
        readers--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        writeRequests++;
        //当没有线程持有读锁+没有线程持有写锁
        while( readers > 0 || writers > 0){
            wait();
        }
        writeRequests--;
        writers++;
    }

    public synchronized void unlockWrite(){
        writers--;
        //多个线程等待读锁，却没有线程等待写锁时，调用了unlockWrite可以一次唤醒多个读
        notifyAll();
    }

}

