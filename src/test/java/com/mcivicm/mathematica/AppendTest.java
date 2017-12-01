package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.Append.append;
import static com.mcivicm.mathematica.NestList.nestList;
import static com.mcivicm.mathematica.Prepend.prepend;
import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/3.
 */

public class AppendTest {
    @Test
    public void name() throws Exception {

        printList(nestList(new Function<List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> apply(List<Integer> list) {
                return append(list, 1);
            }
        }, Arrays.asList(5, 6), 5));
    }

    @Test
    public void name2() throws Exception {

        printList(nestList(new Function<List<Integer>, List<Integer>>() {
            @Override
            public List<Integer> apply(List<Integer> list) {
                return prepend(list, 1);
            }
        }, Arrays.asList(5, 6), 5));
    }
}
