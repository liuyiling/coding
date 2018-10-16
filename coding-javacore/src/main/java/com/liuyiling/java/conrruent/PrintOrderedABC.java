package com.liuyiling.java.conrruent;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.System.out;

/**
 * @author liuyiling
 * @date on 2018/10/16
 */
public class PrintOrderedABC {

    ReentrantLock lock = new ReentrantLock();
    Condition conditionA = lock.newCondition();
    Condition conditionB = lock.newCondition();
    Condition conditionC = lock.newCondition();
    String type = "A";

    void printA() {
        lock.lock();
        try {
            while (!StringUtils.equals(type, "A")) {
                conditionA.await();
            }
            out.println("print A");
        } catch (InterruptedException e) {
        }
        type = "B";
        conditionB.signal();
        lock.unlock();
    }

    void printB() {
        lock.lock();
        try {
            while (!StringUtils.equals(type, "B")) {
                conditionB.await();
            }
            out.println("print B");
        } catch (InterruptedException e) {
        }
        type = "C";
        conditionC.signal();
        lock.unlock();
    }

    void printC() {
        lock.lock();
        try {
            while (!StringUtils.equals(type, "C")) {
                conditionC.await();
            }
            out.println("print C");
        } catch (InterruptedException e) {
        }
        type = "A";
        conditionA.signal();
        lock.unlock();
    }

    public static void main(String[] args) {

        PrintOrderedABC printOrderedABC = new PrintOrderedABC();

        Thread ta = new Thread(() -> {
            printOrderedABC.printA();
        });

        Thread tb = new Thread(() -> {
            printOrderedABC.printB();
        });

        Thread tc = new Thread(() -> {
            printOrderedABC.printC();
        });

        ta.start();
        tb.start();
        tc.start();
    }
}

