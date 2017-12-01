package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiFunction;
import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/4.
 */

public class ArrayTest {
    @Test
    public void name() throws Exception {
        List<Double> list = Array.array(new Function<Double, Double>() {
            @Override
            public Double apply(Double d) {
                return d;
            }
        }, 10, 0, 1);
        printList(list);
    }

    @Test
    public void name1() throws Exception {
        List<List<Double>> list = Array.array(new BiFunction<Double, Double, Double>() {
            @Override
            public Double apply(Double d1, Double d2) {
                return d1 + d2;
            }
        }, 10, 10, 0, 10, 10, 20);
        printList(list);

    }
}
