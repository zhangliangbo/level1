package com.mcivicm.mathematica;

import org.junit.Test;

import java.math.BigInteger;

/**
 * Created by zhang on 2017/9/14.
 */

public class BigIntegerTest {
    @Test
    public void name() throws Exception {
        System.out.println(new BigInteger("1/0", 10));
    }
}
