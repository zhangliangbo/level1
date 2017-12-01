package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiPredicate;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.DeleteDuplicates.deleteDuplicates;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/6.
 */

public class DeleteDuplicatesTest {
    @Test
    public void name() throws Exception {
        printList(deleteDuplicates(Arrays.asList(1, 7, 8, 4, 3, 4, 1, 9, 9, 2)));

    }

    @Test
    public void name1() throws Exception {
        List<Integer> list = DeleteDuplicates.deleteDuplicates(Arrays.asList(1, 7, 8, 4, 3, 4, 1, 9, 9, 2), new BiPredicate<Integer, Integer>() {
            @Override
            public boolean test(Integer integer, Integer integer2) {
                //比前面值小的删掉
                return integer > integer2;
            }
        });
        printList(list);
    }
}
