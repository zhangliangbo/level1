package xxl.mathematica;

import xxl.mathematica.function.BiFunction;
import xxl.mathematica.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格
 */

public class Table {

    /**
     * 生成n个数据的表格
     *
     * @param function
     * @param n
     * @param <R>
     * @return
     */
    public static <R> List<R> table(Function<Integer, R> function, int n) {
        ObjectHelper.requireNonNull(function);
        ObjectHelper.requireNonNegative(n);

        List<R> result = new ArrayList<>(0);
        for (Integer t : Range.range(n)) {
            result.add(function.apply(t));
        }
        return result;
    }

    /**
     * 生成n个数据的表格
     *
     * @param function
     * @param n
     * @param <R>
     * @return
     */
    public static <R> List<R> table(Function<Long, R> function, long n) {
        ObjectHelper.requireNonNull(function);
        ObjectHelper.requireNonNegative(n);

        List<R> result = new ArrayList<>(0);
        for (Long t : Range.range(n)) {
            result.add(function.apply(t));
        }
        return result;
    }

    /**
     * 根据离散点来生成表
     *
     * @param function
     * @param list
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<R> table(Function<T, R> function, List<T> list) {
        ObjectHelper.requireNonNull(function, list);

        List<R> result = new ArrayList<R>(0);
        for (T t : list) {
            result.add(function.apply(t));
        }
        return result;
    }

    /**
     * 根据范围来生成表
     *
     * @param function
     * @param range
     * @param <R>
     * @return
     */
    public static <T, R> List<R> table(Function<T, R> function, Range<T> range) {
        ObjectHelper.requireNonNull(function, range);

        List<R> result = new ArrayList<>(0);
        for (T t : range) {
            result.add(function.apply(t));
        }
        return result;
    }

    /**
     * 根据离散点列表来生成二维表
     *
     * @param function
     * @param list1
     * @param list2
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> List<List<R>> table(BiFunction<T, T, R> function, List<T> list1, List<T> list2) {
        ObjectHelper.requireNonNull(function, list1, list2);

        List<List<R>> result = new ArrayList<>(0);
        for (T t1 : list1) {
            List<R> tempI = new ArrayList<R>();
            for (T t2 : list2) {
                tempI.add(function.apply(t1, t2));
            }
            result.add(tempI);
        }
        return result;
    }

    /**
     * 根据范围生成二维列表
     *
     * @param function
     * @param range1
     * @param range2
     * @param <T>
     * @param <R>
     * @returnT
     */
    public static <T, R> List<List<R>> table(BiFunction<T, T, R> function, Range<T> range1, Range<T> range2) {
        return table(function, (List<T>) range1, (List<T>) range2);
    }
}
