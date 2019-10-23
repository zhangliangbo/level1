package xxl.mathematica.string

import xxl.mathematica.Scan
import xxl.mathematica.function.Function

class StringContainsQTest extends GroovyTestCase {
    void testStringContainQ() {
        Function<String, Boolean> function = StringContainsQ.stringContainQ("a.*")
        def list = ["a", "b", "ab", "abcd", "bcde"]
        Scan.scan({
            t ->
                println(t)
                println(function.apply(t))
        },
                list)
    }
}
