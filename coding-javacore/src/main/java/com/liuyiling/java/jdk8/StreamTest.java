package com.liuyiling.java.jdk8;

import java.util.*;

/**
 * Created by liuyl on 16/8/15.
 */
public class StreamTest {


    public static void main(String[] agrs) {
        sort_original();

        stream_count();
    }

    public static void sort_original() {
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(2);
        list.add(1);

        Collections.sort(list, (o1, o2) -> o1 - o2);
        System.out.println(list);
    }

    public static void stream_count() {

        Collection<Student> students = Arrays.asList(
                new Student(Sex.MALE, 100),
                new Student(Sex.MALE, 80),
                new Student(Sex.FEMAL, 200)
        );

        int totalPointsOfMale = students
                .stream()
                .filter( student -> student.getSex() == Sex.MALE)
                .mapToInt( Student::getGrade)
                .sum();

        System.out.println(totalPointsOfMale);
    }

}
