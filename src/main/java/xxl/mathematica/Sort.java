package xxl.mathematica;

import xxl.mathematica.function.BiFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 排序
 */

public class Sort {

    /**
     * 用排序函数 p 对元素排序
     *
     * @param list
     * @param function
     * @param <T>
     * @return
     */
    public static <T> List<T> sort(List<T> list, BiFunction<T, T, Integer> function) {
        ObjectHelper.requireNonNull(list, "list");
        List<T> result = new ArrayList<>();
        result.addAll(list);
        final BiFunction<T, T, Integer> finalOne = function;
        /**
         * ArrayList#sort() was added in API level 24 and runtimes below API level 24 don't have that method. Looks like your compileSdkVersion is at 24 so you got the code to compile in the first place.
         */
        //记得使用Collections.sort()
        result.sort(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return finalOne.apply(o1, o2);
            }
        });//算法有待改进
        return result;
    }
}
