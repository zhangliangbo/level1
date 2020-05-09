package xxl.mathematica;

import xxl.mathematica.function.Consumer;

import java.util.List;

/**
 * 扫描
 */
public class Scan {
    /**
     * 将函数作用到每个元素
     *
     * @param f
     * @param list
     * @param <T>
     */
    public static <T> void scan(Consumer<T> f, List<T> list) {
        ObjectHelper.requireNonNull(f, list);
        for (T t : list) {
            f.accept(t);
        }
    }
}
