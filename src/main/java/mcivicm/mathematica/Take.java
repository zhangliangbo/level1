package mcivicm.mathematica;

import mcivicm.mathematica.function.BiFunction;

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
        ObjectHelper.requireNonNull(list, "list");
        if (n >= 0) {
            if (list.size() < n) {
                throw new IndexOutOfBoundsException("can not take elements from 0 to " + n + ", the list only have " + list.size() + " elements");
            } else {
                List<T> result = new ArrayList<>(n);
                for (int i = 0; i < n; i++) {
                    result.add(list.get(i));
                }
                return result;
            }
        } else {
            if (list.size() < -n) {
                throw new IndexOutOfBoundsException("can not take elements from " + (list.size() + n) + " to " + list.size() + ", the list only have " + list.size() + " elements");
            } else {
                List<T> result = new ArrayList<>(-n);
                for (int i = list.size() + n; i < list.size(); i++) {
                    result.add(list.get(i));
                }
                return result;
            }
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
        ObjectHelper.requireNonNull(list, "list");
        int s = m;
        if (m < 0) {
            s = m + list.size();
        }
        int e = n;
        if (n < 0) {
            e = n + list.size();
        }
        if (s < 0 || e > list.size() || s > e) {
            throw new IndexOutOfBoundsException("can not take elements from " + m + " to " + n);
        } else {
            List<T> result = new ArrayList<>();
            for (int i = s; i < e; i++) {
                result.add(list.get(i));
            }
            return result;
        }
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
                throw new IndexOutOfBoundsException("can not take elements from " + m + " to " + n + " with step " + step);
            } else {
                List<T> result = new ArrayList<>();
                for (int i = s; i < e; i += step) {
                    result.add(list.get(i));
                }
                return result;
            }
        } else {
            int s = m;
            if (m < 0) {
                s = m + list.size();
            }
            int e = n;
            if (n < 0) {
                e = n + list.size();
            }
            if (e < 0 || s > list.size() || s < e) {
                throw new IndexOutOfBoundsException("can not take elements from " + m + " to " + n + " with step " + step);
            } else {
                List<T> result = new ArrayList<T>(0);
                for (int i = s; i > e; i += step) {//step是负数
                    result.add(list.get(i));
                }
                return result;
            }
        }
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
                throw new IndexOutOfBoundsException("can not extract elements at " + indexList.get(i));
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
        //开始选取
        List<T> result = new ArrayList<>();
        for (Integer integer : sorted) {
            result.add(list.get(integer));
        }
        return result;
    }
}
