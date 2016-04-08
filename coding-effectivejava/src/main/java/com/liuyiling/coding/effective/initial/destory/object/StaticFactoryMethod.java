package com.liuyiling.coding.effective.initial.destory.object;

/**
 * Created by liuyl on 16/3/29.
 * 使用静态工厂方法代替共有的构造函数有三个优点：
 * 优点1：一个类需要多个构造器
 * 优点2：提供一个不可变的类
 * 优点3：返回原类型的子类
 */
public class StaticFactoryMethod {

}

class City {

    private int id;
    private String name;
    private int size;
    private boolean singleton;//是否直辖市

    public City(int id, String name, int size, boolean singleton) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.singleton = singleton;
    }

    //不会变的首都
    public static final City beijing = new City(0, "pangu", 0, true);
    public static final City shanghai = new City(0, "pangu", 0, true);


    /**
     * 优点1：一个类需要多个构造器
     */
    public static City newSingletonCity(int id, String name, int size) {
        City city = new City(id, name, size, true);
        return city;
    }

    public static City newNormalCity(int id, String name, int size) {
        City city = new City(id, name, size, false);
        return city;
    }


    /**
     * 优点2：提供一个不可变的类
     */
    public static City getCaptial() {
        return beijing;
    }


    /**
     * 优点3：返回原类型的子类
     */
    public static City newCountry(int id, String name, int size) {
        return new Country(id, name, size, true);
    }


}

class Country extends City {

    public Country(int id, String name, int size, boolean singleton) {
        super(id, name, size, singleton);
    }
}