package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;
import com.mcivicm.mathematica.predication.FreeQ;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhang on 2017/9/10.
 */

public class FreeQTest {
    @Test
    public void name() throws Exception {
        class Student {
            String name;
        }

        final List<String> names = Arrays.asList("张三", "李四", "王五");
        List<Student> studentList = Array.array(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = names.get(integer);
                return student;
            }
        }, names.size());

        System.out.println("你们班\"风清扬\"是不是转校了? " +
                (FreeQ.freeQ(studentList, new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) {
                        return student.name.equals("风清扬");
                    }
                }) ? "是的" : "还没"));
    }
}
