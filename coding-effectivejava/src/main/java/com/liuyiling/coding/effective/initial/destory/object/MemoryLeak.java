package com.liuyiling.coding.effective.initial.destory.object;

/**
 * Created by liuyl on 16/3/29.
 * 在java体系中：
 *  虽然有了gc垃圾回收机制，但是还是需要管理内存，避免发生内存泄露
 *  很多代码规范从新手的时候就要开始注意了，不然几年之后想改都改不过来
 */
public class MemoryLeak {
}

class Stack{

    private int size = 0;
    private Object[] elements;

    public Stack(Object[] elements) {
        this.elements = new Object[16];
    }

    public void push(Object e){
        elements[size++] = e;
    }

    /**
     * 注意，这一步仅仅删除了数组中的引用对象的地址，并没有释放引用
     * @return
     */
    public Object pop(){
        return elements[size--];
        /**
         * 下面这句话无比关键
         * elements[size] = null
         */
    }
}
