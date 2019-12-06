package xxl.mathematica;

import java.util.Collections;
import java.util.List;

/**
 * 删除
 */

public class Delete {
    /**
     * 用来删除 list 中位置 n 的元素.如果 n 是负数，该位置从表达式的末尾计数.
     *
     * @param list
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> delete(List<T> list, int n) {
        return delete(list, Collections.singletonList(n));
    }

    /**
     * 用来删除位置 index 列表中的部分.
     *
     * @param list
     * @param index
     * @param <T>
     * @return
     */
    public static <T> List<T> delete(List<T> list, List<Integer> index) {
        return Drop.drop(list, index);
    }
}
