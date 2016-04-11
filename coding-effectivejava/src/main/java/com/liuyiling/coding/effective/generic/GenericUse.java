package com.liuyiling.coding.effective.generic;

import java.util.EmptyStackException;

/**
 * Created by liuyl on 16/4/9.
 * 原生态类型指的是Set,仅仅是在jdk1.5之后为了兼容旧的代码才保留的，
 * 新的代码要使用类型安全的集合，如Set<E>
 * 列表优先于数组使用
 * 优先考虑泛型化类
 */
public class GenericUse {
}

class Stack<E>{

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITAL_CAPACITY = 16;

    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITAL_CAPACITY];
    }

    public void push(E e){
        elements[size++] = e;
    }

    public E pop(){
        if(size == 0){
            throw new EmptyStackException();
        }
        E element = elements[--size];
        elements[size] = null;
        return element;
    }

}