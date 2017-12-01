package com.mcivicm.mathematica;

import org.junit.Test;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/1.
 */

public class RangeTest {
    @Test
    public void name() throws Exception {
        printList(Range.range(1.2, 2.2, 0.15));
    }

    @Test
    public void name1() throws Exception {
        printList(Range.range(1, 10, 2));

    }

    @Test
    public void name2() throws Exception {
        printList(Range.range(10, 0, -1));

    }

    @Test
    public void name3() throws Exception {
        printList(Range.range(0, 10, Math.PI));

    }

    @Test
    public void name4() throws Exception {
        printList(Range.range(Math.pow(2, 255), Math.pow(2, 255) + 5));

    }

    @Test
    public void name5() throws Exception {
        printList(Range.range(-4, 9, 3));

    }

}
