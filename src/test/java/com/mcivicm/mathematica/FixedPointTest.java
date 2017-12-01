package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiPredicate;
import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.newton3;

/**
 * Created by zhang on 2017/8/27.
 */

public class FixedPointTest {
    @Test
    public void name1() throws Exception {
        double d = FixedPoint.fixPoint(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return Math.abs(aDouble - aDouble2) < 10e-3;
            }
        });
        System.out.println(d);
    }

    @Test
    public void name2() throws Exception {
        class Student {
            double num;
        }
        Student start = new Student();
        start.num = 1D;
        Student student = FixedPoint.fixPoint(new Function<Student, Student>() {
            @Override
            public Student apply(Student student) {
                Student out = new Student();
                out.num = (student.num + 2D / student.num) / 2D;
                return out;
            }
        }, start, new BiPredicate<Student, Student>() {
            @Override
            public boolean test(Student student, Student student2) {
                return Math.abs(student.num - student2.num) < 10e-3;
            }
        });
        System.out.println("final student: " + student.num);
    }

    @Test
    public void name3() throws Exception {
        double d = FixedPoint.fixPoint(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return Math.abs(aDouble - aDouble2) < 10e-3;
            }
        }, 2);
        System.out.println(d);

    }

    @Test
    public void name4() throws Exception {
        List<Double> list = FixedPointList.fixPointList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return Math.abs(aDouble - aDouble2) < 10e-3;
            }
        }, 2);
        System.out.print(Arrays.toString(list.toArray()));
    }

    @Test
    public void name5() throws Exception {
        List<Double> list = FixedPointList.fixPointList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return Math.abs(aDouble - aDouble2) < 10e-8;
            }
        });
        System.out.print(Arrays.toString(list.toArray()));
    }

    @Test
    public void name6() throws Exception {
        double d = FixedPoint.fixPoint(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, 10e-8);
        System.out.println(d);
    }

    @Test
    public void name7() throws Exception {

        List<Double> list = FixedPointList.fixPointList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2;
            }
        }, 1D, 10e-15);
        System.out.print(Arrays.toString(list.toArray()));
    }

    @Test
    public void name8() throws Exception {
        List<Double> list = FixedPointList.fixPointList(newton3, 1D);
        System.out.println(Arrays.toString(list.toArray()));
    }
}
