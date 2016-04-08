package com.liuyiling.java.staticProxy;

/**
 * Created by liuyl on 15/12/18.
 * 静态代理类，如果需要对多个类进行代理，17、19行会有代码重复
 * 不是一种好的方法
 */
public class CalculatorProxy {


    private Calculator calculator;

    public CalculatorProxy(Calculator calculator) {
        this.calculator = calculator;
    }

    public int add(int a, int b) {
        //do something before proxyed class execute real method
        int result = calculator.add(a ,b);
        //do something after proxyed class execute real method
        return result;
    }
}
