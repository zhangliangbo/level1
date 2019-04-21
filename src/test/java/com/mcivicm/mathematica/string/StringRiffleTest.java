package com.mcivicm.mathematica.string;

import com.mcivicm.mathematica.RandomInteger;
import com.mcivicm.mathematica.function.Function;
import org.junit.Test;

import java.util.Arrays;

public class StringRiffleTest {

    @Test
    public void all() {
        System.out.println(
                StringRiffle.stringRiffle(
                        RandomInteger.randomInteger(0, 10, 1000),
                        new Function<Integer, String>() {
                            @Override
                            public String apply(Integer integer) {
                                return "xxl" + integer;
                            }
                        },
                        "(",
                        ": ",
                        ")"));
    }

    @Test
    public void nullable() {
        System.out.println(StringRiffle.stringRiffle(null, "|"));
    }

    @Test
    public void defaultSplitter() {
        System.out.println(StringRiffle.stringRiffle(Arrays.asList("zlb", "hwj", "dsx")));
    }

    @Test
    public void splitter() {
        System.out.println(StringRiffle.stringRiffle(Arrays.asList("zlb", "hwj", "dsx"), "|"));
    }
}
