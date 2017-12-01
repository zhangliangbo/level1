package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Consumer;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by zhang on 2017/9/22.
 */

public class DoTest {
    @Test
    public void name() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, 5);

    }

    @Test
    public void name1() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, 1, 10);

    }

    @Test
    public void name2() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, 1, 10, -1);

    }

    @Test
    public void name3() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, 1, 10, 3);

    }

    @Test
    public void name4() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, 10, -3, -3);

    }

    @Test
    public void name5() throws Exception {
        Do.loop(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                System.out.println(integer);
            }
        }, Arrays.asList(1,4,5,9,4));

    }
}
