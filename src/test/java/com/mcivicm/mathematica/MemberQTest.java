package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;
import com.mcivicm.mathematica.predication.MemberQ;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhang on 2017/9/10.
 */

public class MemberQTest {

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

        System.out.println("你们班有没有叫王五的? " +
                (MemberQ.memberQ(studentList, new Predicate<Student>() {
                    @Override
                    public boolean test(Student student) {
                        return student.name.equals("王五");
                    }
                }) ? "有" : "没有"));
    }


}
