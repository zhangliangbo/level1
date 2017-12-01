package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/5.
 */

public class TakeWhileTest {
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
        printList(TakeWhile.takeWhile(list, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                return student.name.contains("1");
            }
        }));

    }

    @Test
    public void name2() throws Exception {
        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = String.valueOf(integer);
                return student;
            }
        }, 10);
        printList(list);
        printList(Take.take(list, -3));
    }

    @Test
    public void name3() throws Exception {
        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = String.valueOf(integer);
                return student;
            }
        }, 10);
        printList(list);
        printList(Take.take(list, -1, -5, -1));
    }

    @Test
    public void name4() throws Exception {
        List<Student> list = Table.table(new Function<Integer, Student>() {
            @Override
            public Student apply(Integer integer) {
                Student student = new Student();
                student.name = String.valueOf(integer);
                return student;
            }
        }, 10);
        printList(list);
        printList(Extract.extract(list, Arrays.asList(-1, -5, -1, 3, 6)));
    }
}
