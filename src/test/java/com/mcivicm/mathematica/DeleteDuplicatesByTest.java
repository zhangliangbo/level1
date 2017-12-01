package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/6.
 */

public class DeleteDuplicatesByTest {
    private class Student {
        String name;
        int score;

        @Override
        public String toString() {
            return name + score;
        }
    }

    @Test
    public void name() throws Exception {
        List<Student> studentList = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = "学生党";
                student.score = integer;
                return student;
            }
        }, Arrays.asList(1, 7, 8, 4, 3, 4, 1, 9, 9, 2));
        printList(studentList);
        List<Student> list = DeleteDuplicatesBy.deleteDuplicatesBy(studentList, new Function<Student, Integer>() {
            @Override
            public Integer apply(Student student) {
                return student.score;
            }
        });
        printList(list);
    }
}
