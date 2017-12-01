package com.mcivicm.mathematica;

import org.junit.Test;

import static com.mcivicm.mathematica.And.and;

/**
 * Created by zhang on 2017/9/11.
 */

public class AndTest {
    @Test
    public void name() throws Exception {
        System.out.println(and(new Boolean[0]));
        System.out.println(and(1 > 0, 0 > 1, 1 + 1 == 2));
        System.out.println(and(1 + 1 == 2, 2 + 2 == 4));
        System.out.println(and(1 + 1 == 3, 2 + 2 == 3));
        System.out.println(and((Boolean[]) null));
    }
}
