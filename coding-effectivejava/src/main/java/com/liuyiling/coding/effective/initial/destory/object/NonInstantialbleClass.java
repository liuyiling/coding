package com.liuyiling.coding.effective.initial.destory.object;

/**
 * Created by liuyl on 16/1/11.
 * 通过私有构造器,使得该类不可以被实例化
 * 也就是我们传说中得单例模式，保证了整个系统中都共用了一个对象
 * 如文件管理系统中的桌面（一只智能有一个对象）
 */
public class NonInstantialbleClass {

    public static void main(String[] agrs){
        NonInstantialble nonInstantialble = NonInstantialble.getInstance();
    }

}

class NonInstantialble{

   public static final NonInstantialble NON_INSTANTIALBLE = new NonInstantialble();

    /**
     * 私有的构造方法
     */
    private NonInstantialble() {
    }
    public static NonInstantialble getInstance(){
        return NON_INSTANTIALBLE;
    }
}

