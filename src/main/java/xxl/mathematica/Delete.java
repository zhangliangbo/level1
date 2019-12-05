package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除
 */

public class Delete {
    /**
     * 用来删除 expr 中位置 n 的元素.如果 n 是负数，该位置从表达式的末尾计数.
     *
     * @param list
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> delete(List<T> list, int n) {
        ObjectHelper.requireNonNull(list, "list");
        int positive = n;
        if (n < 0) {
            positive = n + list.size();
        }
        if (positive < 0 || positive >= list.size()) {
            throw new IndexOutOfBoundsException("can not delete element at " + n);
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < positive; i++) {
            result.add(list.get(i));
        }
        for (int i = positive + 1; i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
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
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(index, "list");
        List<Integer> positive = new ArrayList<>();
        for (Integer integer : index) {
            int raw = integer;
            if (integer < 0) {
                raw = integer + list.size();
            }
            if (raw < 0 || raw >= list.size()) {
                throw new IndexOutOfBoundsException("can not delete element at " + integer);
            }
            positive.add(raw);
        }
        //在相应的位置删除数据
        List<T> result = new ArrayList<>();
        if (positive.size() == 0) {
            result.addAll(list);
            return result;
        } else if (positive.size() == 1) {
            return delete(list, positive.get(0));
        } else {
            //先排个序
            List<Integer> sorted = Sort.sort(positive);
            //索引将列表分成了index.size()+1段
            for (int i = 0; i < sorted.get(0); i++) {
                result.add(list.get(i));
            }
            for (int i = 0; i < sorted.size() - 1; i++) {
                //跳过要删除的数据
                for (int j = sorted.get(i) + 1; j < sorted.get(i + 1); j++) {
                    result.add(list.get(j));
                }
            }
            //跳过要删除的数据
            for (int i = sorted.get(sorted.size() - 1) + 1; i < list.size(); i++) {
                result.add(list.get(i));
            }
            return result;
        }
    }
}
