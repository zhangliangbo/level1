package mcivicm.mathematica;

import mcivicm.mathematica.function.Predicate;

import java.util.ArrayList;
import java.util.List;


/**
 * 位置
 */

public class Position {

    /**
     * 给出在 list 中匹配 criteria 的对象的位置列表.
     *
     * @param list
     * @param criteria
     * @param <T>
     * @return
     */
    public static <T> List<Integer> position(List<T> list, Predicate<T> criteria) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (criteria.test(list.get(i))) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * 给出找到的前 n 个对象的位置
     *
     * @param list
     * @param criteria
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<Integer> position(List<T> list, Predicate<T> criteria, int n) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(criteria, "criteria");
        ObjectHelper.requireNonNegative(n, "n");

        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (criteria.test(list.get(i))) {
                result.add(i);
                if (result.size() >= n) {
                    return result;
                }
            }
        }
        return result;
    }
}
