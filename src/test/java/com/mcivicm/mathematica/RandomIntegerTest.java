package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;
import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

;

/**
 * Created by zhang on 2017/9/10.
 */

public class RandomIntegerTest {
    @Test
    public void name() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        printList(Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger();
            }
        }, 100));
    }

    @Test
    public void name4() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        printList(Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger(-10);
            }
        }, 100));
    }

    @Test
    public void name5() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        printList(Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger(10);
            }
        }, 100));
    }

    @Test
    public void name1() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        List<Integer> list = Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger(-10, 10);
            }
        }, 100);
        printList(list);
        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer <= -10;
            }
        }));

        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 10;
            }
        }));
    }

    @Test
    public void name2() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        List<Integer> list = Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger(10, -10);
            }
        }, 100);
        printList(list);
        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 10;
            }
        }));

        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer <= -10;
            }
        }));
    }

    @Test
    public void name3() throws Exception {
        System.out.println(RandomInteger.randomInteger());
        printList(Table.table(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return RandomInteger.randomInteger(10, 10);
            }
        }, 100));
    }

    @Test
    public void name6() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(-10, 10, 100);
        printList(list);
        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer <= -10;
            }
        }));

        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 10;
            }
        }));
    }

    @Test
    public void name7() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(10, -10, 100);
        printList(list);
        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >= 10;
            }
        }));

        printList(Select.select(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer <= -10;
            }
        }));
    }

    @Test
    public void name8() throws Exception {
        printList(RandomInteger.randomInteger(10, 10, 100));
    }
}
