package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * 选择
 */

public class Select {
    /**
     * 选取 list 中使得 criteria 为 True 的所有元素.
     *
     * @param list
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> List<T> select(List<T> list, Predicate<T> criteria) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (criteria.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * 选取 list 中使得 criteria 为 True 的前 n 个元素.
     *
     * @param list
     * @param criteria
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> select(List<T> list, Predicate<T> criteria, int n) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");
        ObjectHelper.requireNonNegative(n, "n");
        List<T> result = new ArrayList<>();
        if (n == 0) {
            return result;
        }
        for (T t : list) {
            if (criteria.test(t)) {
                result.add(t);
                if (result.size() >= n) {
                    return result;
                }
            }
        }
        return result;
    }
}
