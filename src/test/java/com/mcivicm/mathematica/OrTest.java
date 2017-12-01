package com.mcivicm.mathematica;

import org.junit.Test;

import static com.mcivicm.mathematica.Or.or;

/**
 * Created by zhang on 2017/9/11.
 */

public class OrTest {
    @Test
    public void name() throws Exception {
        System.out.println(or(new Boolean[0]));
        System.out.println(or(1 > 0, 0 > 1, 1 + 1 == 2));
        System.out.println(or(1 + 1 == 2, 2 + 2 == 4));
        System.out.println(or(1 + 1 == 3, 2 + 2 == 3));
        System.out.println(or((Boolean[]) null));
    }
}
