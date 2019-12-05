package xxl.mathematica;

import java.util.ArrayList;
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
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(t, "t");
        int positive = n;
        if (n < 0) {
            positive += list.size();
        }
        if (positive < 0 || positive > list.size()) {
            throw new IndexOutOfBoundsException("can not insert element at " + n);
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < positive; i++) {
            result.add(list.get(i));
        }
        result.add(t);
        for (int i = positive; i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
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
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(t, "t");
        ObjectHelper.requireNonNull(index, "positionList");
        //转成正向索引
        List<Integer> positive = new ArrayList<>();
        for (Integer integer : index) {
            int raw = integer;
            if (integer < 0) {
                raw = integer + list.size();
            }
            if (raw < 0 || raw > list.size()) {
                throw new IndexOutOfBoundsException("can not insert element at " + integer);
            }
            positive.add(raw);
        }
        //在相应的位置插入数据
        List<T> result = new ArrayList<>();
        if (index.size() == 0) {
            result.addAll(list);
            return result;
        } else if (index.size() == 1) {
            return insert(list, t, positive.get(0));
        } else {
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
            return result;
        }
    }
}
