package xxl.mathematica;

import xxl.mathematica.function.Function;

import java.util.List;

/**
 * 应用
 */

public class Apply {
    /**
     * 应用函数到列表中
     *
     * @param function
     * @param list
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> R apply(Function<List<T>, R> function, List<T> list) {
        ObjectHelper.requireNonNull(function, "function");
        ObjectHelper.requireNonNull(list, "list");
        return function.apply(list);
    }
}
