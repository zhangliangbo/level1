package com.mcivicm.mathematica.predication;

import com.mcivicm.mathematica.ObjectHelper;
import com.mcivicm.mathematica.function.Predicate;

import java.util.List;

/**
 * 成员判定
 */

public class MemberQ {
    /**
     * 用来判断 list 的一个元素是否与 criteria 匹配，若是，则返回 True，否则返回 False.
     *
     * @param list
     * @param criteria 判断的标准
     * @param <T>
     * @return
     */
    public static <T> boolean memberQ(List<T> list, Predicate<T> criteria) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");

        for (T t : list) {
            if (criteria.test(t)) {
                return true;
            }
        }
        return false;
    }
}
