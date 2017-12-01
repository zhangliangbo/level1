package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/11.
 */

public class MapTest {
    @Test
    public void name() throws Exception {
        List<Double> list = Map.map(new Function<Integer, Double>() {
            @Override
            public Double apply(Integer integer) {
                return Math.pow(integer, 2);
            }
        }, Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        printList(list);
    }
}
