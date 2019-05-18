package mcivicm.mathematica


import org.junit.Test

import static mcivicm.mathematica.BaseTest.printList
import static mcivicm.mathematica.BitXor.bitXor
import static mcivicm.mathematica.Map.map

/**
 * Created by zhang on 2017/9/12.
 */

class BitXOrTest {
    @Test
    void name() throws Exception {
        printList(map({ Integer[] ints -> bitXor(ints) }, [[], [61], [61, 15], [61, 15, 13], [3333, 5555, 7777, 9999], [-61, 15]]))
    }
}
