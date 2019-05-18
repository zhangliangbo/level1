package mcivicm.mathematica;

import mcivicm.mathematica.function.BiFunction;

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
