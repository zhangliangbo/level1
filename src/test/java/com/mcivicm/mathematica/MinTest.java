package com.mcivicm.mathematica;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/14.
 */

public class MinTest {
    @Test
    public void name() throws Exception {
        System.out.println(Min.min(9, 2));
        List<Integer> list = RandomInteger.randomInteger(0, 100, 10);
        printList(list);
        System.out.println(Min.min(list.toArray(new Integer[list.size()])));
    }

    @Test
    public void name1() throws Exception {
        BigInteger bigInteger = Min
                .min(new BigInteger("170141183460469231731687303715884105727"),
                        new BigInteger("170141183460469231731687303715884105726"),
                        new BigInteger("17"),
                        new BigInteger("170141183460469231731687"),
                        new BigInteger("17014118346046923173168730371588410572"),
                        new BigInteger("170141183460469231731687303715884105725251563148562")
                );
        System.out.println(bigInteger);
    }

    @Test
    public void name2() throws Exception {
        System.out.println(Min.min(new BigDecimal[0]));
    }
}
