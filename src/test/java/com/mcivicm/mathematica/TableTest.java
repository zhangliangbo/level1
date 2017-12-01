package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiFunction;
import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.Range.range;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/3.
 */

public class TableTest {

    class Student {
        String name;

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    public void name() throws Exception {

        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = "name" + integer;
                return student;
            }
        }, range(10));
        for (Student student : list) {
            System.out.println(student.name);
        }
    }

    @Test
    public void name1() throws Exception {

        printList(Table.table(new BiFunction<Integer, Integer, Student>() {

            @Override
            public Student apply(Integer integer, Integer integer2) {
                Student student = new Student();
                student.name = "name" + integer + "" + integer2;
                return student;
            }
        }, range(10), range(10)));
    }

    @Test
    public void name2() throws Exception {

        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = "name" + integer;
                return student;
            }
        }, range(3, 100, 2));
        for (Student student : list) {
            System.out.println(student.name);
        }

    }

    @Test
    public void name3() throws Exception {
        printList(Table.table(new BiFunction<Integer, Integer, Student>() {

            @Override
            public Student apply(Integer integer, Integer integer2) {
                Student student = new Student();
                student.name = "name" + integer + "" + integer2;
                return student;
            }
        }, range(4, 8, 3), range(8, 11, 1)));

    }

    @Test
    public void name4() throws Exception {
        printList(Table.table(new BiFunction<Integer, Integer, Student>() {
            @Override
            public Student apply(Integer integer, Integer integer2) {
                Student student = new Student();
                student.name = "name" + integer + "" + integer2;
                return student;
            }
        }, Arrays.asList(1, 3, 5), Arrays.asList(2, 4, 6)));
    }
}
