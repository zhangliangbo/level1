package xxl.mathematica.predication;

import xxl.mathematica.ObjectHelper;
import xxl.mathematica.function.Predicate;

import java.util.List;


/**
 * 判定是否不存在匹配
 */

public class FreeQ {
    /**
     * 如果在 expr 中没有匹配 criteria 的子表达式，则生成 True，否则生成 False.
     *
     * @param list
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> boolean freeQ(List<T> list, Predicate<T> criteria) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");
        for (T t : list) {
            if (criteria.test(t)) {
                return false;
            }
        }
        return true;
    }
}
