package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 加在前面
 */

public class Prepend {
    /**
     * 在 list 前加 elem.
     *
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> prepend(List<T> list, T t) {
        ObjectHelper.requireNonNull(list, t);
        List<T> result = new ArrayList<>();
        result.add(t);
        result.addAll(list);
        return result;
    }
}
