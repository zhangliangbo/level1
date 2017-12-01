package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;

import static com.mcivicm.mathematica.BitOr.bitOr;
import static com.mcivicm.mathematica.Map.map;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/12.
 */

public class BitOrTest {
    @Test
    public void name() throws Exception {
        printList(map(new Function<Integer[], Integer>() {
            @Override
            public Integer apply(Integer[] ints) {
                return bitOr(ints);
            }
        }, Arrays.asList(new Integer[]{}, new Integer[]{61}, new Integer[]{61, 15}, new Integer[]{61, 15, 13},
                new Integer[]{3333, 5555, 7777, 9999})));
    }



}
