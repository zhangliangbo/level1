package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhang on 2017/9/1.
 */

public class BaseTest {

    public static Function<Double, Double> newton3 = new Function<Double, Double>() {
        @Override
        public Double apply(Double aDouble) {
            return 1D * (aDouble + 3D / aDouble) / 2D;
        }
    };

    public static Function<Integer, Integer> divide2 = new Function<Integer, Integer>() {
        @Override
        public Integer apply(Integer aDouble) {
            return aDouble / 2;
        }
    };

    protected static void printList(List list) {
        System.out.println(Arrays.toString(list.toArray()));
    }
}
