package com.liuyiling.java.basic;

/**
 * Created by liuyl on 15/11/24.
 */
public class DeepCloneAndSimpleClone {


    public static void main(String[] agrs) throws CloneNotSupportedException {
        DeepClone deepClone = new DeepClone();
        deepClone.a = 12;
        DeepClone deepClone2 = (DeepClone) deepClone.clone();
        System.out.println(deepClone2.a);


        //浅拷贝
        B b1 = new B();
        System.out.println(b1.obj.a);

        B b2 = (B) b1.clone();
        b2.obj.a = 12;
        System.out.println(b1.obj.a);

    }
}

//Cloneable只是一个标识
class DeepClone implements Cloneable {

    public int a = 0;

    //clone为native方法，效率高
    @Override
    protected Object clone() {
        DeepClone deepClone = null;
        try {
            deepClone = (DeepClone) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return deepClone;
    }
}

class A implements Cloneable{
    public int a = 11;

    @Override
    protected Object clone() {
        A result = null;
        try {
            result = (A) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return result;
    }
}

class B implements Cloneable {
    public int b = 0;
    A obj = new A();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        B result = null;
        try {
            result = (B) super.clone();
            //深拷贝多这一句
            //result.obj = (A) obj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return result;
    }
}