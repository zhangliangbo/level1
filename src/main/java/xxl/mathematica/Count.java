package xxl.mathematica;

import xxl.mathematica.function.Predicate;

import java.util.List;

/**
 * 计数
 */

public class Count {
    /**
     * 给出 list 中匹配 criteria 的元素个数.
     * @param list
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> int count(List<T> list, Predicate<T> criteria) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");
        int count = 0;
        for (T t : list) {
            if (criteria.test(t)) {
                count++;
            }
        }
        return count;
    }
}
