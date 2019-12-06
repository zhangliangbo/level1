package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 去掉元素
 */

public class Drop {
    /**
     * 去掉list的前n个或者后n个元素
     *
     * @param list
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> drop(List<T> list, int n) {
        if (n < 0) {
            return drop(list, list.size() + n, list.size());
        } else {
            return drop(list, 0, n);
        }
    }

    /**
     * 去掉 list 的从 m（包括） 到 n（不包括） 的元素
     *
     * @param list
     * @param m
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> drop(List<T> list, int m, int n) {
        return drop(list, m, n, 1);
    }

    /**
     * 去掉 list 的从m（包括） 到 n（不包括） 的元素，步长为 s
     *
     * @param list
     * @param m
     * @param n
     * @param step
     * @param <T>
     * @return
     */
    public static <T> List<T> drop(List<T> list, int m, int n, int step) {
        ObjectHelper.requireNonNull(list);
        ObjectHelper.requireNonZero(step, "step");

        int s = m < 0 ? m + list.size() : m;
        int e = n < 0 ? n + list.size() : n;
        if (s < 0 || s > list.size() || e < 0 || e > list.size() || (step < 0 && s > e) || (step > 0 && s < e)) {
            throw new IndexOutOfBoundsException("can not drop elements from " + m + " to " + n);
        }
        //首先计算删除索引
        List<Integer> index = new ArrayList<>();
        for (int i = s; i > e; i += step) {
            index.add(i);
        }
        return drop(list, index);
    }

    /**
     * 去掉 list 在索引表处的值
     *
     * @param list
     * @param indexList
     * @param <T>
     * @return
     */
    public static <T> List<T> drop(List<T> list, List<Integer> indexList) {
        ObjectHelper.requireNonNull(list, indexList);
        if (indexList.size() == 0) {//没有元素需要删除
            return new ArrayList<>();
        }
        //转成正向索引
        List<Integer> positive = Map.map(t -> t < 0 ? t + list.size() : t, indexList);
        //检查索引的合法性
        Scan.scan(t -> {
            if (t < 0 || t > list.size()) {
                throw new IndexOutOfBoundsException("can not drop elements at " + t);
            }
        }, positive);
        //删除重复的索引
        List<Integer> noDuplicates = DeleteDuplicates.deleteDuplicates(positive);
        //对索引排序
        List<Integer> sorted = Sort.sort(noDuplicates);
        //开始移除
        List<T> result = new ArrayList<>();
        //把列表分成sorted.size()+1段
        for (int i = 0; i < sorted.get(0); i++) {
            result.add(list.get(i));
        }
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = sorted.get(i) + 1; j < sorted.get(i + 1); j++) {
                result.add(list.get(j));
            }
        }
        for (int i = sorted.get(sorted.size() - 1) + 1; i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
    }
}
