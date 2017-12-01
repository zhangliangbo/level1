package com.mcivicm.mathematica;

import org.junit.Test;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/5.
 */

public class SubdivideTest {
    @Test
    public void name() throws Exception {
        printList(Subdivide.subdivide(100));
    }

    @Test
    public void name1() throws Exception {
        printList(Subdivide.subdivide(-100, 100));
    }

    @Test
    public void name2() throws Exception {
        printList(Subdivide.subdivide(-100, 100, 100));
    }
}
