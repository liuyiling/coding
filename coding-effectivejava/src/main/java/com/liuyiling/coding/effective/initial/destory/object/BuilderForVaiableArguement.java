package com.liuyiling.coding.effective.initial.destory.object;

/**
 * Created by liuyl on 16/1/10.
 * builder构造器模式：
 *  多个参数并且有很大一部分是可选参数的时候，可以使用构建器
 *  优点有：
 *      1.避免书写大量的构造函数
 *      2.方便以后在类中添加字段（成员变量）
 * eg.ThreadLocalPool
 */
public class BuilderForVaiableArguement {

    public static void main(String[] agrs){

        People people = new People
                .Builder("liuyiling", 24, 1)
                .car("bmw730")
                .build();

        System.out.println(people.getCar());

    }

}

/**
 * 人有很多属性，必要的:name,age,sex
 * 不必要的:car,creidt,wife,husband,son,daughter
 * 不必要的参数个数大于必要的参数个数时,考虑用构建器
 */
class People {

    //必要参数
    private String name;
    private int age;
    private int sex;

    //不必要参数
    private String car;
    private String creidt;
    private String wife;
    private String husband;
    private String son;
    private String daughter;

    private People(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.sex = builder.sex;


        this.car = builder.car;
        this.creidt = builder.creidt;
        this.wife = builder.wife;
        this.husband = builder.husband;
        this.son = builder.son;
        this.daughter = builder.daughter;

    }

    public static class Builder {

        //必要参数
        private String name;
        private int age;
        private int sex;


        //不必要参数,所有默认的参数值都放在这
        private String car = null;
        private String creidt = null;
        private String wife = null;
        private String husband = null;
        private String son = null;
        private String daughter = null;


        public Builder(String name, int age, int sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }


        public Builder car(String car){
            this.car = car;
            return this;
        }

        public Builder creidt(String creidt){
            this.creidt = creidt;
            return this;
        }

        public Builder wife(String wife){
            this.wife = wife;
            return this;
        }

        public Builder husband(String husband){
            this.husband = husband;
            return this;
        }

        public Builder son(String son){
            this.son = son;
            return this;
        }

        public Builder daughter(String daughter){
            this.daughter = daughter;
            return this;
        }

        public People build() {

            return new People(this);
        }
    }


    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getSex() {
        return sex;
    }

    public String getCar() {
        return car;
    }

    public String getCreidt() {
        return creidt;
    }

    public String getWife() {
        return wife;
    }

    public String getHusband() {
        return husband;
    }

    public String getSon() {
        return son;
    }

    public String getDaughter() {
        return daughter;
    }
}

