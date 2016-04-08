package com.liuyiling.guava.collection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * Created by liuyl on 16/4/7.
 * 在日常的开发工作中，我们有的时候需要构造像Map<K, List<V>>或者Map<K, Set<V>>这样比较复杂的集合类型的数据结构，以便做相应的业务逻辑处理。
 * 这种情况下可以使用MultiMap来进行处理
 */
public class MultiMapUse {

    public static void main(String[] agrs) {

        /**
         * <学生,课程成绩列表>的传统实现方式
         */
        Map<String, List<StudentScore>> studentScoreMap = new HashMap<>();

        for (int i = 0; i < 20; i++) {
            List<StudentScore> scoreList = studentScoreMap.get("liuyiling");

            if (scoreList == null) {
                List<StudentScore> scoreTemp = new ArrayList<>();
                studentScoreMap.put("liuyiling", scoreTemp);
            }

            scoreList = studentScoreMap.get("liuyiling");
            StudentScore studentScore = new StudentScore(i, i+100);
            scoreList.add(studentScore);

        }


        /**
         * 采用multiMap的简单方式
         */
        Multimap<String, StudentScore> scoreMap = ArrayListMultimap.create();
        for (int i = 0; i < 20; i++) {
            StudentScore studentScore = new StudentScore(i, i + 100);
            scoreMap.put("liuyiling", studentScore);
        }

        System.out.println("scoreMap:" + scoreMap.size());
        System.out.println("scoreMap:" + scoreMap.keys());

        /**
         * 调用Multimap.get(key)会返回这个键对应的值的集合的视图（view），没有对应集合就返回空集合。对于ListMultimap来说，这个方法会返回一个List，对于SetMultimap来说，这个方法就返回一个Set。修改数据是通过修改底层Multimap来实现的
         * 返回的类型根据ArrayListMultimap还是SetMultiMap来区分
         */
        Collection<StudentScore> liuyilingScores = scoreMap.get("liuyiling");
        System.out.println(liuyilingScores.size());

    }


    static class StudentScore {
        private int courseId;
        private int grade;

        public StudentScore(int courseId, int grade) {
            this.courseId = courseId;
            this.grade = grade;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }
    }

}
