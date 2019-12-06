package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 追加
 */

public class Append {
    /**
     * 列表 list 末尾添加 t
     *
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> append(List<T> list, T t) {
        ObjectHelper.requireNonNull(list, t);
        List<T> result = new ArrayList<>(list);
        result.add(t);
        return result;
    }
}
