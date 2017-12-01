package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.BiPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * 删除重复元素
 */

public class DeleteDuplicates {
    /**
     * 删除 list 中的重复元素
     * 不对元素重排序，仅删除它们
     *
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<T> deleteDuplicates(List<T> list) {
        ObjectHelper.requireNonNull(list, "list");
        if (list.size() == 0) {
            return new ArrayList<>();
        } else if (list.size() == 1) {//一个元素不存在重复值
            List<T> result = new ArrayList<>();
            result.addAll(list);
            return result;
        } else {
            List<T> result = new ArrayList<>();
            result.add(list.get(0));//第一个元素肯定不和其他元素重复
            for (int i = 1; i < list.size(); i++) {
                boolean containsQ = false;
                for (int j = 0; j < result.size(); j++) {
                    if (list.get(i).equals(result.get(j))) {
                        containsQ = true;
                        break;
                    }
                }
                if (!containsQ) {
                    result.add(list.get(i));
                }
            }
            return result;
        }
    }

    /**
     * 将 test 应用到元素对中，确定它们是否是重复的
     * 不对元素重排序，仅删除它们
     *
     * @param list
     * @param test
     * @param <T>
     * @return
     */
    public static <T> List<T> deleteDuplicates(List<T> list, BiPredicate<T, T> test) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(test, "test");

        if (list.size() == 0) {
            return new ArrayList<>();
        } else if (list.size() == 1) {//一个元素不存在重复值
            List<T> result = new ArrayList<>();
            result.addAll(list);
            return result;
        } else {
            List<T> result = new ArrayList<>();
            result.add(list.get(0));
            for (int i = 1; i < list.size(); i++) {
                boolean containsQ = false;
                for (int j = 0; j < result.size(); j++) {
                    //从左到右分别是列表【前面的值】和列表【当前值】
                    if (test.test(result.get(j), list.get(i))) {
                        containsQ = true;
                        break;
                    }
                }
                if (!containsQ) {
                    result.add(list.get(i));
                }
            }
            return result;
        }
    }
}
