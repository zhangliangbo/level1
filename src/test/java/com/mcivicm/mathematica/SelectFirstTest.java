package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/5.
 */

public class SelectFirstTest {
    @Test
    public void name() throws Exception {
        List<Integer> origin = Range.range(100);
        printList(origin);
        System.out.println(SelectFirst.selectFirst(origin, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 98;
            }
        }));
        System.out.println(SelectFirst.selectFirst(origin, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 100;
            }
        }, -1));//选择前10个
    }
}
