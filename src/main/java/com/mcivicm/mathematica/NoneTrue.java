package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import java.util.List;

/**
 * 非真判定
 */

public class NoneTrue {
    /**
     * 如果 test[Subscript[e, i]] 是 False（对于所有 Subscript[e, i]）,产生 True。
     *
     * @param list
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean noneTrue(List<T> list, Predicate<T> predicate) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(predicate, "predicate");

        for (T t : list) {
            if (predicate.test(t)) {
                return false;
            }
        }

        return true;
    }
}
