package mcivicm.mathematica

import org.junit.Test

import static mcivicm.mathematica.BaseTest.printList
import static mcivicm.mathematica.BitAnd.bitAnd
import static mcivicm.mathematica.Map.map

/**
 * Created by zhang on 2017/9/12.
 */

class BitAndTest {
    @Test
    void name() throws Exception {
        printList(map({ Integer[] ints -> bitAnd(ints) }, [[], [61], [61, 15], [61, 15, 13], [3333, 5555, 7777, 9999]]))
    }


}
