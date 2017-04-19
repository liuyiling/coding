package com.liuyiling.coding.effective.generic;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

/**
 * Created by liuyl on 16/4/9.
 * 原生态类型指的是Set,仅仅是在jdk1.5之后为了兼容旧的代码才保留的，
 * 新的代码要使用类型安全的集合，如Set<E>
 * 列表优先于数组使用
 * 优先考虑泛型化类
 */
public class GenericUse {

    public static void main(String[] agrs) {
        GenericMethodTest.printArray(new Integer[]{1, 2, 3});
        GenericMethodTest.printArray(new Float[]{1.1f, 2.2f, 3.3f});

        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        GenericMethodTest.printArrayForNumber(integers);

        List<String> strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        //GenericMethodTest.printArrayForNumber(strings);

        System.out.println(GenericMethodTest.maximun(1, 2));
        System.out.println(GenericMethodTest.maximun(1.2, 2.1));

    }

}

/**
 * 泛型类的使用
 *
 * @param <E>
 */
class Stack<E> {

    private E[] elements;
    private int size = 0;
    private static final int DEFAULT_INITAL_CAPACITY = 16;

    public Stack() {
        elements = (E[]) new Object[DEFAULT_INITAL_CAPACITY];
    }

    public void push(E e) {
        elements[size++] = e;
    }

    public E pop() {
        if (size == 0) {
            throw new EmptyStackException();
        }
        E element = elements[--size];
        elements[size] = null;
        return element;
    }
}


class GenericMethodTest {

    /**
     * 泛型方法的使用-1
     */
    public static <E> void printArray(E[] printArray) {
        for (E element : printArray) {
            System.out.printf("%s", element);
        }
    }

    /**
     * 泛型方法的使用-2
     */
    public static <E extends Comparable> E maximun(E x, E y) {
        E max = x;
        if (y.compareTo(max) > 0) {
            max = y;
        }

        return max;
    }

    /**
     * 泛型方法的使用-3
     */
    public static <T extends Number> T printArrayForNumber(List<? extends Number> printArray) {
        T result = (T) printArray.get(0);
        return result;
    }

}

