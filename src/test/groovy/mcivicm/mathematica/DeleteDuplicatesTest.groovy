package mcivicm.mathematica


import org.junit.Test

import static mcivicm.mathematica.BaseTest.printList
import static mcivicm.mathematica.DeleteDuplicates.deleteDuplicates

/**
 * Created by zhang on 2017/9/6.
 */

class DeleteDuplicatesTest {

    @Test
    void name() throws Exception {
        printList(deleteDuplicates([1, 7, 8, 4, 3, 4, 1, 9, 9, 2]))
    }

    @Test
    void name1() throws Exception {
        List<Integer> list = deleteDuplicates([1, 7, 8, 4, 3, 4, 1, 9, 9, 2], { a, b -> a > b })
        printList(list)
    }
}
