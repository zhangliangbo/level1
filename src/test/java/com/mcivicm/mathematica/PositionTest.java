package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import org.junit.Test;

import java.util.List;

import static com.mcivicm.mathematica.BaseTest.printList;

/**
 * Created by zhang on 2017/9/10.
 */

public class PositionTest {
    @Test
    public void name() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(10, 20, 100);
        printList(list);
        List<Integer> position = Position.position(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 15;
            }
        });
        printList(position);
        printList(Extract.extract(list, position));
        printList(TakeWhile.takeWhile(Drop.drop(list, position), new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 15;
            }
        }));
    }

    @Test
    public void name1() throws Exception {
        List<Integer> list = RandomInteger.randomInteger(10, 20, 100);
        printList(list);
        List<Integer> position = Position.position(list, new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 15;
            }
        }, 5);//取前5
        printList(position);
        printList(Extract.extract(list, position));
        printList(TakeWhile.takeWhile(Drop.drop(list, position), new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                return integer > 15;
            }
        }));
    }
}
