package mcivicm.mathematica

import mcivicm.mathematica.function.Function

import org.junit.Test

import static mcivicm.mathematica.BitNot.bitNot
import static mcivicm.mathematica.Map.map
import static mcivicm.mathematica.BaseTest.printList
import static java.util.Arrays.asList

/**
 * Created by zhang on 2017/9/12.
 */

class BitNotTest {
    @Test
    void name() throws Exception {
        printList(map(new Function<Integer, Integer>() {
            @Override
            Integer apply(Integer i) {
                return bitNot(i)
            }
        }, asList(0, 61, 15, 13, -61, -15, -13)))
    }


}
