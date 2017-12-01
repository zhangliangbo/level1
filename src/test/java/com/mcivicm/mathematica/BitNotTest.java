package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import static com.mcivicm.mathematica.BitNot.bitNot;
import static com.mcivicm.mathematica.Map.map;
import static com.mcivicm.mathematica.BaseTest.printList;
import static java.util.Arrays.asList;

/**
 * Created by zhang on 2017/9/12.
 */

public class BitNotTest {
    @Test
    public void name() throws Exception {
        printList(map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer i) {
                return bitNot(i);
            }
        }, asList(0, 61, 15, 13, -61, -15, -13)));
    }


}
