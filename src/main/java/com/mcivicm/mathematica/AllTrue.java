package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import java.util.List;

/**
 * 全真
 */

public class AllTrue {
    /**
     * 如果对于所有 Subscript[e, i]，test[Subscript[e, i]] 均为 True，则生成 True.
     *
     * @param list
     * @param predicate
     * @param <T>
     * @return
     */
    public static <T> boolean allTrue(List<T> list, Predicate<T> predicate) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(predicate, "predicate");
        for (T t : list) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }
}
