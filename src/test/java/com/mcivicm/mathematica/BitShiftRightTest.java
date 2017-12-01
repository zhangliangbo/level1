package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.math.BigInteger;

import static com.mcivicm.mathematica.BitShiftRight.bitShiftRight;
import static com.mcivicm.mathematica.Map.map;
import static java.util.Arrays.asList;

/**
 * Created by zhang on 2017/9/12.
 */

public class BitShiftRightTest {
    @Test
    public void name() throws Exception {
        System.out.println(map(new Function<Integer[], Integer>() {
            @Override
            public Integer apply(Integer[] integer) {
                return bitShiftRight(integer[0], integer[1]);
            }
        }, asList(new Integer[]{32, 3}, new Integer[]{1, 2}, new Integer[]{2, 2}, new Integer[]{4, 2}, new Integer[]{16, 2})));

    }

    @Test
    public void name1() throws Exception {
        System.out.println(map(new Function<Integer[], Integer>() {
                                   @Override
                                   public Integer apply(Integer[] integer) {
                                       //两者的实现还是有区别的
                                       return Integer.rotateRight(integer[0], integer[1]);
                                   }
                               }, asList(new Integer[]{32, 3},
                new Integer[]{1, 2},
                new Integer[]{2, 2},
                new Integer[]{4, 2},
                new Integer[]{16, 2})
        ));

    }

    @Test
    public void name3() throws Exception {
        System.out.println(new BigInteger("-1", 10));
    }

    @Test
    public void name2() throws Exception {
        System.out.println(
                bitShiftRight(
                        new BigInteger("1606938044258990275541962092341162602522202993782792835301376", 10)
                                .add(new BigInteger("-1", 10)), 7));
        System.out.println("12554203470773361527671578846415332832204710888928069025791");

    }
}
