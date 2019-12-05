package xxl.mathematica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序
 */

public class Sort {
    /**
     * 枚举单例
     */
    private enum NaturalOrderComparator implements Comparator<Comparable<Object>> {
        INSTANCE;

        @Override
        public int compare(Comparable<Object> c1, Comparable<Object> c2) {
            return c1.compareTo(c2);
        }

        @Override
        public Comparator<Comparable<Object>> reversed() {
            return Comparator.reverseOrder();
        }
    }

    /**
     * 自然排序算子
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder() {
        return (Comparator<T>) NaturalOrderComparator.INSTANCE;
    }

    /**
     * 用排序函数 p 对元素排序
     *
     * @param list
     * @param p
     * @param <T>
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> sort(List<T> list, Comparator<T> p) {
        ObjectHelper.requireNonNull(list, "list");
        List<T> result = new ArrayList<>(list);
        /**
         * ArrayList#sort() was added in API level 24 and runtimes below API level 24 don't have that method. Looks like your compileSdkVersion is at 24 so you got the code to compile in the first place.
         */
        //后续考虑自己写算法
        Collections.sort(result, p);
        return result;
    }

    /**
     * 自然顺序
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T extends Comparable<? super T>> List<T> sort(List<T> list) {
        return sort(list, naturalOrder());
    }
}
