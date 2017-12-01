package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/11.
 */

public class AllTest {
    @Test
    public void name() throws Exception {
        class Student {
            int score;
        }
        final List<Integer> score = RandomInteger.randomInteger(0, 100, 10);
        printList(score);
        List<Student> list = Array.array(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.score = score.get(integer);
                return student;
            }
        }, 10);
        System.out.println("你们班都及格？ " + (AllTrue.allTrue(list, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.score >= 60;
            }
        }) ? "是" : "否"));

        System.out.println("你们班有一个及格？ " + (AnyTrue.anyTrue(list, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.score >= 60;
            }
        }) ? "是" : "否"));

        System.out.println("你们班有多少人及格？ " + (Count.count(list, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.score >= 60;
            }
        })));

        System.out.println("你们班没有人及格？ " + (NoneTrue.noneTrue(list, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.score >= 60;
            }
        }) ? "是" : "否"));
    }
}
