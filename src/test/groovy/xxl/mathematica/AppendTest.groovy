package xxl.mathematica

import xxl.mathematica.function.Function
import org.junit.Test

import static xxl.mathematica.Append.append
import static xxl.mathematica.BaseTest.printList
import static xxl.mathematica.NestList.nestList
import static xxl.mathematica.Prepend.prepend
/**
 * Created by zhang on 2017/9/3.
 */

class AppendTest {
    @Test
    void name() throws Exception {

        printList(nestList(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return append(list, 1)
            }
        }, Arrays.asList(5, 6), 5))
    }

    @Test
    void name2() throws Exception {

        printList(nestList(new Function<List<Integer>, List<Integer>>() {
            @Override
            List<Integer> apply(List<Integer> list) {
                return prepend(list, 1)
            }
        }, Arrays.asList(5, 6), 5))
    }
}