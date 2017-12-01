package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.newton3;

/**
 * Created by zhang on 2017/8/27.
 */

public class NestTest {
    @Test
    public void name1() throws Exception {
        double d = Nest.nest(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (1 + aDouble) * (1 + aDouble);
            }
        }, 1D, 3);
        System.out.println(d);
    }

    @Test
    public void name2() throws Exception {
        double d = Nest.nest(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.sqrt(aDouble);
            }
        }, 100., 4);
        System.out.println(d);
    }

    @Test
    public void name3() throws Exception {
        double d = Nest.nest(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                if (aDouble > 10e6) {
                    System.out.println(aDouble);
                    return aDouble;
                } else {
                    return aDouble * aDouble;
                }
            }
        }, 2D, 6);
    }

    @Test
    public void name4() throws Exception {
        List<Double> d = NestList.nestList(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return Math.sqrt(aDouble);
            }
        }, 100., 4);
        System.out.println(Arrays.toString(d.toArray()));
    }

    @Test
    public void name5() throws Exception {
        //在 10 年内每年复合资本的增长：
        double d = Nest.nest(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (1 + 0.05) * aDouble;
            }
        }, 1000., 10);
        System.out.println(d);
    }

    @Test
    public void name6() throws Exception {
        //根号2的牛顿法迭代
        double d = Nest.nest(new Function<Double, Double>() {
            @Override
            public Double apply(Double aDouble) {
                return (aDouble + 2D / aDouble) / 2D;
            }
        }, 1., 5);
        System.out.println(d);
    }



    @Test
    public void name8() throws Exception {
        List<Double> list = NestList.nestList(newton3, 1D, 5);
        System.out.println(Arrays.toString(list.toArray()));
    }
}
