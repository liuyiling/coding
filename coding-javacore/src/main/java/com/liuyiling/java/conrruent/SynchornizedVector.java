package com.liuyiling.java.conrruent;

import java.util.Vector;

/**
 * Created by liuyl on 15/11/29.
 */
public class SynchornizedVector {

    public static void main(String[] agrs) {
        Vector vector = new Vector();

        for (int i = 0; i < 10; i++) {
            vector.add(i, i);
        }


        new Thread() {
            @Override
            public void run() {
                //vector共有10个元素，index对应0-9
                //第一步：线程1执行到j=8，暂停;

                synchronized (SynchornizedVector.class) {
                    for (int j = 0; j < vector.size(); j++) {
                        //第三部，线程1继续执行，要获取vector.get(8)的时候出错，因为vector的元素已经被线程2清空
                        if (j == 8) {
                            try {
                                Thread.currentThread().sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        System.out.println(vector.get(j));
                    }
                }
            }
        }.start();

        new Thread() {

            @Override
            public void run() {
                //第二步：线程2获得时间片，立即执行，删除掉vector中所有的元素

                synchronized (SynchornizedVector.class) {
                    for (int i = 0; i < vector.size(); i++) {
                        vector.remove(i);
                    }
                }

            }
        }.start();
    }
}
