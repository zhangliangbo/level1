package com.mcivicm.mathematica.string;

import com.mcivicm.mathematica.function.Function;
import org.junit.Test;

public class StringRepeatTest {
    @Test
    public void name() {
        System.out.println(StringRepeat.stringRepeat("a", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return "<" + s + ">";
            }
        }, 50));
    }

    @Test
    public void max() {
        System.out.println(StringRepeat.stringRepeat("a", new Function<String, String>() {
            @Override
            public String apply(String s) {
                return s;
            }
        }, 50, 50));
    }


    @Test
    public void maxNoConvert() {
        System.out.println(StringRepeat.stringRepeat("abc", 10, 19));
    }
}