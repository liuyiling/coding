package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/25.
 */
public class InnerClassTest {

    public static void main(String[] agrs){

        OuterClass outerClass = new OuterClass();
        OuterClass.InnerClass innerClass = outerClass.new InnerClass();
        innerClass.writeLine();

    }

}


class OuterClass{


    private int a = 0;

    class InnerClass{
        private int a = 1;

        public void writeLine(){

            //匿名内部类
            new Thread(){
                @Override
                public void run() {
                    System.out.println("thread: " + a);
                }
            }.start();

            System.out.println("OuterClass a: " + OuterClass.this.a);
            System.out.println("InnerClass a: " + this.a );
        }
    }

}