package com.liuyiling.java.jdk8;

/**
 * Created by liuyl on 16/8/16.
 */
public class Student {

    private Sex sex;
    private int grade;

    public Student(Sex sex, int grade) {
        this.sex = sex;
        this.grade = grade;
    }

    public Sex getSex() {
        return sex;
    }

    public int getGrade() {
        return grade;
    }

}

enum Sex{
    MALE,FEMAL
};

