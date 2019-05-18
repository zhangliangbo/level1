package mcivicm.mathematica;

import mcivicm.mathematica.function.BiFunction;

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
        ObjectHelper.requireNonNull(list, "list");

        if (n >= 0) {
            if (list.size() < n) {
                throw new IndexOutOfBoundsException("can not drop element from 0 to " + n + ", the list only have " + list.size() + " elements");
            }
            List<T> result = new ArrayList<>();
            for (int i = n; i < list.size(); i++) {
                result.add(list.get(i));
            }
            return result;
        } else {
            if (list.size() < -n) {
                throw new IndexOutOfBoundsException("can not drop element from " + (list.size() + n) + " to " + list.size() + ", the list only have " + list.size() + " elements");
            }
            List<T> result = new ArrayList<>();
            for (int i = 0; i < list.size() + n; i++) {
                result.add(list.get(i));
            }
            return result;
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
        ObjectHelper.requireNonNull(list, "list");
        int s = m;
        if (m < 0) {
            s = m + list.size();
        }
        int e = n;
        if (n < 0) {
            e = n + list.size();
        }
        if (s < 0 || n > list.size() || s > e) {
            throw new IndexOutOfBoundsException("can not drop elements from " + m + " to " + n);
        }
        List<T> result = new ArrayList<>();
        //加入前部分
        for (int i = 0; i < s; i++) {
            result.add(list.get(i));
        }
        //加入后部分
        for (int i = e; i < list.size(); i++) {
            result.add(list.get(i));
        }
        return result;
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
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonZero(step, "step");

        if (step >= 0) {
            int s = m;
            if (m < 0) {
                s = m + list.size();
            }
            int e = n;
            if (n < 0) {
                e = n + list.size();
            }
            if (s < 0 || e > list.size() || s > e) {
                throw new IndexOutOfBoundsException("can not drop elements from " + m + " to " + n);
            }
            //首先计算删除索引
            List<Integer> index = new ArrayList<>();
            for (int i = m; i < n; i += step) {
                index.add(i);
            }
            return drop(list, index);
        } else {
            int s = m;
            if (m < 0) {
                s = m + list.size();
            }
            int e = n;
            if (n < 0) {
                e = n + list.size();
            }
            if (s > list.size() || e < 0 || e > s) {
                throw new IndexOutOfBoundsException("can not drop elements from " + m + " to " + n);
            }
            //首先计算删除索引
            List<Integer> index = new ArrayList<>();
            for (int i = s; i > e; i += step) {
                index.add(i);
            }
            return drop(list, index);
        }
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
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(indexList, "indexList");
        if (indexList.size() == 0) {//没有元素需要删除
            List<T> result = new ArrayList<>();
            result.addAll(list);
            return result;
        }
        //转成正向索引
        List<Integer> positiveIndex = new ArrayList<>();
        for (Integer integer : indexList) {
            positiveIndex.add(integer < 0 ? integer + list.size() : integer);
        }
        //检查索引的合法性
        for (int i = 0; i < positiveIndex.size(); i++) {
            Integer integer = positiveIndex.get(i);
            if (integer < 0 || integer > list.size()) {
                throw new IndexOutOfBoundsException("can not drop elements at " + indexList.get(i));
            }
        }
        //删除重复的索引
        List<Integer> noDuplicates = DeleteDuplicates.deleteDuplicates(positiveIndex);
        //对索引排序
        List<Integer> sorted = Sort.sort(noDuplicates, new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) {
                return integer.compareTo(integer2);
            }
        });
        //开始移除
        List<T> result = new ArrayList<>();
        //把列表分成sorted.size()+1段
        if (sorted.size() == 1) {
            for (int i = 0; i < sorted.get(0); i++) {
                result.add(list.get(i));
            }
            for (int i = sorted.get(0) + 1; i < list.size(); i++) {
                result.add(list.get(i));
            }
            return result;
        } else {
            //0到第一个节点
            for (int i = 0; i < sorted.get(0); i++) {
                result.add(list.get(i));
            }
            //第一个节点到第N个节点
            for (int i = 0; i < sorted.size() - 1; i++) {
                for (int j = sorted.get(i) + 1; j < sorted.get(i + 1); j++) {
                    result.add(list.get(j));
                }
            }
            //第N个节点到最后
            for (int i = sorted.get(sorted.size() - 1) + 1; i < list.size(); i++) {
                result.add(list.get(i));
            }
            return result;
        }
    }
}
