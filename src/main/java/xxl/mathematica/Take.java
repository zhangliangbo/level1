package xxl.mathematica;

import java.util.ArrayList;
import java.util.List;

/**
 * 选取
 */

public class Take {
    /**
     * 给出list的前n个或者后n个元素
     *
     * @param list
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> take(List<T> list, int n) {
        if (n < 0) {
            return take(list, list.size() + n, list.size());
        } else {
            return take(list, 0, n);
        }
    }

    /**
     * 给出 list 中从 m（包括） 到 n（不包括） 的元素
     *
     * @param list
     * @param m
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> take(List<T> list, int m, int n) {
        return take(list, m, n, 1);
    }

    /**
     * 给出 list 中从 m（包括） 到 n（不包括） 的元素，步长为step
     *
     * @param list
     * @param m
     * @param n
     * @param <T>
     * @return
     */
    public static <T> List<T> take(List<T> list, int m, int n, int step) {
        ObjectHelper.requireNonNull(list);
        ObjectHelper.requireNonZero(step, "step");
        int s = m < 0 ? m + list.size() : m;
        int e = n < 0 ? n + list.size() : n;
        if (s < 0 || s > list.size() || e < 0 || e > list.size() || (step < 0 && s > e) || (step > 0 && s < e)) {
            throw new IndexOutOfBoundsException("can not take elements from " + m + " to " + n + " with step " + step);
        }
        List<Integer> index = new ArrayList<>();
        for (int i = s; i < e; i += step) {
            index.add(i);
        }
        return take(list, index);
    }

    /**
     * 提取 list 在索引表处的值
     *
     * @param list
     * @param indexList
     * @param <T>
     * @return
     */
    public static <T> List<T> take(List<T> list, List<Integer> indexList) {
        ObjectHelper.requireNonNull(list, indexList);
        if (indexList.size() == 0) {//没有元素需要删除
            return new ArrayList<>();
        }
        //转成正向索引
        List<Integer> positive = Map.map(integer -> integer < 0 ? integer + list.size() : integer, indexList);
        //检查索引的合法性
        Scan.scan(integer -> {
            if (integer < 0 || integer > list.size()) {
                throw new IndexOutOfBoundsException("can not extract elements at " + integer);
            }
        }, positive);
        //删除重复的索引
        List<Integer> noDuplicates = DeleteDuplicates.deleteDuplicates(positive);
        //对索引排序
        List<Integer> sorted = Sort.sort(noDuplicates);
        //开始选取
        return Map.map(list::get, sorted);
    }
}
