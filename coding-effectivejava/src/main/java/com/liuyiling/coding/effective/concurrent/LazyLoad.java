package com.liuyiling.coding.effective.concurrent;

/**
 * Created by liuyl on 16/1/12.
 * 大多数情况下的域都应该正常初始化
 * 如果因为性能问题需要延迟初始化，对于实例域，使用double check
 * 对于静态域，使用lazy initialization holder class idiom
 */
public class LazyLoad {


    public static void main(String[] agrs){

        /**
         * 实例域的lazy load
         */
        LazyLoadTest lazy = new LazyLoadTest();

        for ( int i = 0 ; i < 100 ; i++){
            new Thread(()->{
                lazy.getObject();
            }
            ).start();
        }


        /**
         * 静态域的lazy load
          */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Object field = FieldHoler.field;
    }

    private static class FieldHoler {

        static {
            System.out.println("FieldHoler!");
        }

        /**
         * 对某一个静态域lazy initial
         */
        static final Object field = computeFieldValue();

        private static Object computeFieldValue() {
            /**
             * 复杂的初始化过程
             */
            return null;
        }

    }

    public static Object getField(){
        return FieldHoler.field;
    }

}

class LazyLoadTest{

    private Object object;

    public Object getObject(){
        /**
         * 双重检查机制
         */
        Object result = object;
        if (result == null) {
            synchronized (this) {
                result = object;
                if( result == null ){
                    object = computeFieldValue();
                }
            }
        }
        return null;
    }

    private Object computeFieldValue() {
        System.out.println("复杂的初始化过程");
        /**
         * 复杂的初始化过程
         */
        return new Object();
    }

}


