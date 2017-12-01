package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiFunction;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/1.
 */

public class FoldTest {
    @Test
    public void name() throws Exception {
        //找出最大值
        Integer i = Fold.fold(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return Math.max(a, b);
            }
        }, 0, new Integer[]{1, 3, 1, 2, 4, 1, 5, 3, 6, 2, 8, 11});
        System.out.print(i);
    }

    @Test
    public void name2() throws Exception {
        //列表的列表，嵌套列表
        List list = Fold.fold(new BiFunction<List, List, List>() {
            @Override
            public List apply(List list1, List list2) {
                List<List> lists = new ArrayList<>(0);
                lists.add(list1);
                lists.add(list2);
                return lists;
            }
        }, Arrays.asList(1, 2), new List[]{Arrays.asList(3, 4), Arrays.asList(5, 6), Arrays.asList(7, 8)});
        printList(list);
    }

    @Test
    public void name3() throws Exception {
        //列表的列表，嵌套列表
        List list = Fold.fold(new BiFunction<List, List, List>() {
            @Override
            public List apply(List list1, List list2) {
                List<List> lists = new ArrayList<>(0);
                lists.add(list1);
                lists.add(list2);
                return lists;
            }
        }, new List[]{Arrays.asList(1, 2),Arrays.asList(3, 4), Arrays.asList(5, 6), Arrays.asList(7, 8)});
        printList(list);
    }
}
