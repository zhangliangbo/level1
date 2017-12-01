package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/5.
 */

public class TakeDropTest {
    private class Student {

        String name = "default";

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    public void name1() throws Exception {
        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = String.valueOf(integer);
                return student;
            }
        }, 10);
        printList(list);
        printList(TakeDrop.takeDrop(list, 3));
        printList(TakeDrop.takeDrop(list, -3));
        printList(TakeDrop.takeDrop(list, 3, 5));
        printList(TakeDrop.takeDrop(list, -1, -10, -2));
        printList(TakeDrop.takeDrop(list, 1, 10, 2));
    }
}
