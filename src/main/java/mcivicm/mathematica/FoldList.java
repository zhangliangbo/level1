package mcivicm.mathematica;

import mcivicm.mathematica.function.BiFunction;

import java.util.ArrayList;
import java.util.List;

/**
 * 折叠列表
 */

public class FoldList {
    /**
     * 给出 {x,f[x,a],f[f[x,a],b]}.
     *
     * @param function
     * @param initValue
     * @param list      列表
     * @param <T>
     * @return
     */
    public static <T> List<T> foldList(BiFunction<T, T, T> function, T initValue, List<T> list) {

        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNull(initValue, "initValue");
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireLengthNotLessThan(list, 1, "list");

        List<T> result = new ArrayList<T>(1);
        result.add(initValue);
        T last = initValue;//上一个是第一个
        for (int i = 0; i < list.size(); i++) {
            T cur = list.get(i);
            T next = function.apply(last, cur);
            result.add(next);
            last = next;//重置上一个
        }
        return result;
    }

    /**
     * 给出 {x,f[x,a],f[f[x,a],b]}.
     *
     * @param function
     * @param initValue
     * @param array     数组
     * @param <T>
     * @return
     */
    public static <T> List<T> foldList(BiFunction<T, T, T> function, T initValue, T[] array) {

        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNull(initValue, "initValue");
        ObjectHelper.requireNonNull(array, "list");
        ObjectHelper.requireLengthNotLessThan(array, 1, "array");

        List<T> result = new ArrayList<T>(1);
        result.add(initValue);
        T last = initValue;//上一个是第一个
        for (int i = 0; i < array.length; i++) {
            T cur = array[i];
            T next = function.apply(last, cur);
            result.add(next);
            last = next;//重置上一个
        }
        return result;
    }

    /**
     * 给出 {x,f[x,a],f[f[x,a],b]}.
     *
     * @param function
     * @param list     列表
     * @param <T>
     * @return
     */
    public static <T> List<T> foldList(BiFunction<T, T, T> function, List<T> list) {

        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireLengthNotLessThan(list, 2, "list");

        List<T> result = new ArrayList<T>(1);
        result.add(list.get(0));
        T last = list.get(0);//上一个是第一个
        for (int i = 1; i < list.size(); i++) {
            T cur = list.get(i);
            T next = function.apply(last, cur);
            result.add(next);
            last = next;//重置上一个
        }
        return result;
    }

    /**
     * 给出 {x,f[x,a],f[f[x,a],b]}.
     *
     * @param function
     * @param array    数组
     * @param <T>
     * @return
     */
    public static <T> List<T> foldList(BiFunction<T, T, T> function, T[] array) {

        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNull(array, "array");
        ObjectHelper.requireLengthNotLessThan(array, 2, "list");

        List<T> result = new ArrayList<T>(1);
        result.add(array[0]);
        T last = array[0];//上一个是第一个
        for (int i = 1; i < array.length; i++) {
            T cur = array[i];
            T next = function.apply(last, cur);
            result.add(next);
            last = next;//重置上一个
        }
        return result;
    }
}
