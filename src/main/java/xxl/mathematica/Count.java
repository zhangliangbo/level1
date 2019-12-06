package xxl.mathematica;

import xxl.mathematica.function.Predicate;

import java.util.List;

/**
 * 计数
 */

public class Count {
    /**
     * 给出 list 中匹配 p 的元素个数.
     *
     * @param list
     * @param p
     * @param <T>
     * @return
     */
    public static <T> int count(List<T> list, Predicate<T> p) {
        ObjectHelper.requireNonNull(list, p);
        int count = 0;
        for (T t : list) {
            if (p.test(t)) {
                count++;
            }
        }
        return count;
    }
}
