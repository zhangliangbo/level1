package com.mcivicm.mathematica;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mcivicm.mathematica.Delete.delete;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/3.
 */

public class DeleteTest {

    @Test
    public void name2() throws Exception {
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), 0));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), 1));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), 2));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), 3));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), 4));
        System.out.println("from end");
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -1));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -2));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -3));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -4));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -5));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), -6));
    }

    @Test
    public void name3() throws Exception {
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), new ArrayList<Integer>()));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(0)));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(0, 1, 2)));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(1, 3, 4)));
        printList(delete(Arrays.asList(1, 2, 3, 4, 5), Arrays.asList(-1, -3, -4)));
    }
}
