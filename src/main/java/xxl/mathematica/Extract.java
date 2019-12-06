package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 提取
 */

public class Extract {
    /**
     * 提取 list 在索引表处的值
     *
     * @param list
     * @param indexList
     * @param <T>
     * @return
     */
    public static <T> List<T> extract(List<T> list, List<Integer> indexList) {
        ObjectHelper.requireNonNull(list, indexList);
        if (indexList.size() == 0) {
            return new ArrayList<>(list);
        }
        //转成正向索引
        List<Integer> positive = Map.map(t -> t < 0 ? t + list.size() : t, indexList);
        //检查索引的合法性
        Scan.scan(t -> {
            if (t < 0 || t > list.size()) {
                throw new IndexOutOfBoundsException("can not extract elements at " + t);
            }
        }, positive);
        //开始选取
        return Map.map(list::get, positive);
    }
}
