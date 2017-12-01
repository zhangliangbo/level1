package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/10.
 */

public class FirstPositionTest {
    @Test
    public void name() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(10, 20, 100);
        printList(list);
        int position = FirstPosition.firstPosition(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 15;
            }
        });
        System.out.println(position);
    }

    @Test
    public void name1() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(10, 20, 100);
        printList(list);
        int position = FirstPosition.firstPosition(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer >=20;
            }
        }, -1);//取前5
        System.out.println(position);
    }
}
