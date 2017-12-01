package com.mcivicm.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 追加
 */

public class Append {
    /**
     * 给出追加 elem 的 expr
     *
     * @param list
     * @param t
     * @param <T>
     * @return
     */
    public static <T> List<T> append(List<T> list, T t) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(t, "t");
        List<T> result = new ArrayList<>();
        result.addAll(list);
        result.add(t);
        return result;
    }
}
