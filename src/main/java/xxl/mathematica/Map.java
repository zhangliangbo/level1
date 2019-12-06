package xxl.mathematica;

import xxl.mathematica.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * 映射
 */

public class Map {
    /**
     * 将 f 应用到 list 的每个元素.
     *
     * @param function
     * @param list
     * @param <T>
     * @return
     */
    public static <T, R> List<R> map(Function<T, R> function, List<T> list) {
        ObjectHelper.requireNonNull(function, list);
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(function.apply(t));
        }
        return result;
    }
}
