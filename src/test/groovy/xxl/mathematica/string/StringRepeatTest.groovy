package xxl.mathematica.string

import xxl.mathematica.function.Function
import org.junit.Test

class StringRepeatTest {
    @Test
    void name() {
        System.out.println(StringRepeat.stringRepeat("a", new Function<String, String>() {
            @Override
            String apply(String s) {
                return "<" + s + ">"
            }
        }, 50))
    }

    @Test
    void max() {
        System.out.println(StringRepeat.stringRepeat("a", new Function<String, String>() {
            @Override
            String apply(String s) {
                return s
            }
        }, 50, 50))
    }


    @Test
    void maxNoConvert() {
        System.out.println(StringRepeat.stringRepeat("abc", 10, 19))
    }
}