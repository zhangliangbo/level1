package com.mcivicm.mathematica.string;

import com.mcivicm.mathematica.RandomInteger;
import com.mcivicm.mathematica.function.Function;
import org.junit.Test;

public class StringJoinTest {
    @Test
    public void all() {
        System.out.println(
                StringJoin.stringJoin(
                        RandomInteger.randomInteger(0, 10, 25),
                        new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) {
                                return "我是数" + integer;
                            }
                        }
                )
        );
    }
}
