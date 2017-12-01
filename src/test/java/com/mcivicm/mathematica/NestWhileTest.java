package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiPredicate;
import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;
import com.mcivicm.mathematica.predication.EvenQ;
import com.mcivicm.mathematica.predication.PrimerQ;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.divide2;
import static com.mcivicm.mathematica.BaseTest.newton3;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/8/27.
 */

public class NestWhileTest {
    @Test
    public void name() throws Exception {
        int i = NestWhile.nestWhile(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer / 2;
            }
        }, 123456, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return EvenQ.evenQ(integer);
            }
        });
        System.out.println("一直除以2直到不为偶数:" + i);
    }

    @Test
    public void name1() throws Exception {
        List<Integer> i = NestWhileList.nestWhileList(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer / 2;
            }
        }, 123456, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return EvenQ.evenQ(integer);
            }
        });
        System.out.println("一直除以2直到不为偶数:" + Arrays.asList(i.toArray()));
    }

    @Test
    public void name2() throws Exception {
        double d = NestWhile.nestWhile(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 3D / aDouble) / 2D;
            }
        }, 1., new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {//如果最后两个结果不相等，就一直计算，直到相等为止
                System.out.println("middle: " + aDouble + ", " + aDouble2);
                return aDouble.doubleValue() != aDouble2.doubleValue();
            }
        });
        System.out.println("final: " + d);
    }

    @Test
    public void name3() throws Exception {
        int i = NestWhile.nestWhile(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer / 2;
            }
        }, 123456, new Predicate<List<Integer>>() {
            @Override
            public boolean test(List<Integer> integers) {
                return EvenQ.evenQ(integers.get(0));
            }
        }, 1);
        System.out.println("一直除以2直到不为偶数:" + i);
    }

    @Test
    public void name4() throws Exception {
        double d = NestWhile.nestWhile(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 3D / aDouble) / 2D;
            }
        }, 1., new Predicate<List<Double>>() {
            @Override
            public boolean test(List<Double> doubles) {
                System.out.println("middle: " + doubles.get(0) + ", " + doubles.get(1));
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2);
        System.out.println("final: " + d);

    }

    @Test
    public void name5() throws Exception {
        int d = NestWhile.nestWhile(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer + 1;
            }
        }, 888, new Predicate<List<Integer>>() {
            @Override
            public boolean test(List<Integer> doubles) {
                return !(PrimerQ.primerQ(doubles.get(0)) && PrimerQ.primerQ(doubles.get(2)));
            }
        }, 3);
        System.out.println("888后的孪生素数对：" + d + ", " + (d + 2));
    }

    @Test
    public void name6() throws Exception {
        double d = NestWhile.nestWhile(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new Predicate<List<Double>>() {
            @Override
            public boolean test(List<Double> doubles) {
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2);
        System.out.print("重复运用Cos到初始值1，直到结果不改变为止: " + d);
    }

    @Test
    public void name7() throws Exception {
        double d = NestWhile.nestWhile(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new Predicate<List<Double>>() {
            @Override
            public boolean test(List<Double> doubles) {
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2, 3);
        System.out.print("重复运用Cos到初始值1，3次，的结果为: " + d);

    }

    @Test
    public void name8() throws Exception {
        List<Double> list = NestWhileList.nestWhileList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return aDouble.doubleValue() != aDouble2.doubleValue();
            }
        });
        System.out.println("重复运用Cos到初始值1，直到相邻两数相等: ");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + ":" + list.get(i));
        }

    }

    @Test
    public void name9() throws Exception {
        List<Double> list = NestWhileList.nestWhileList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new Predicate<List<Double>>() {
            @Override
            public boolean test(List<Double> doubles) {
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2, 100, -94);//
        System.out.println("重复运用Cos到初始值1，直到相邻两数相等: " + "length: " + list.size());
    }


    @Test
    public void same1() throws Exception {
        List<Double> list = NestWhileList.nestWhileList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new Predicate<List<Double>>() {
            @Override
            public boolean test(List<Double> doubles) {
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2);//
        System.out.println("重复运用Cos到初始值1，直到相邻两数相等: " + "length: " + list.size());
    }

    @Test
    public void same2() throws Exception {
        List<Double> list = NestWhileList.nestWhileList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.cos(aDouble);
            }
        }, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return aDouble.doubleValue() != aDouble2.doubleValue();
            }
        });//
        System.out.println("重复运用Cos到初始值1，直到相邻两数相等: " + "length: " + list.size());
    }

    @Test
    public void divide2() throws Exception {
        printList(NestWhileList.nestWhileList(divide2, 123456, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return EvenQ.evenQ(integer);
            }
        }));
    }

    @Test
    public void newton3() throws Exception {
        printList(NestWhileList.nestWhileList(newton3, 1D, new BiPredicate<Double, Double>() {
            @Override
            public boolean test(Double aDouble, Double aDouble2) {
                return aDouble.doubleValue() != aDouble2.doubleValue();
            }
        }));
        printList(NestWhileList.nestWhileList(newton3, 1D, new Predicate<List<Double>>() {

            @Override
            public boolean test(List<Double> doubles) {
                return doubles.get(0).doubleValue() != doubles.get(1).doubleValue();
            }
        }, 2));
    }
}
