package xxl.mathematica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 插入
 */

public class Insert {
    /**
     * 在 list 中的位置 n 上插入 elem. 如果 n 为负，位置从结尾计算
     *
     * @param list
     * @param t
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> insert(List<T> list, T t, int n) {
        return insert(list, t, Collections.singletonList(n));
    }

    /**
     * 在 expr 中的位置 {i,j,...} 上插入 elem.
     *
     * @param list
     * @param t
     * @param index
     * @param <T>
     * @return
     */
    public static <T> List<T> insert(List<T> list, T t, List<Integer> index) {
        ObjectHelper.requireNonNull(list, t, index);
        //转成正向索引
        List<Integer> positive = new ArrayList<>();
        Scan.scan(i -> {
            int raw = i < 0 ? i + list.size() : i;
            if (raw < 0 || raw > list.size()) {
                throw new IndexOutOfBoundsException("can not insert element at " + i);
            }
            positive.add(raw);
        }, index);
        //在相应的位置插入数据
        List<T> result = new ArrayList<>();
        if (index.size() != 0) {
            //先排个序
            List<Integer> sorted = Sort.sort(positive);
            //索引将列表分成了index.size()+1段
            for (int i = 0; i < sorted.get(0); i++) {
                result.add(list.get(i));
            }
            for (int i = 0; i < sorted.size() - 1; i++) {
                result.add(t);//插入数据
                for (int j = sorted.get(i); j < sorted.get(i + 1); j++) {
                    result.add(list.get(j));
                }
            }
            result.add(t);//插入数据
            for (int i = sorted.get(sorted.size() - 1); i < list.size(); i++) {
                result.add(list.get(i));
            }
        }
        return result;
    }
}
