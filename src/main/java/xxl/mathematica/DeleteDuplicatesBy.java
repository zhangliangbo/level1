package xxl.mathematica;

import xxl.mathematica.function.Function;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据指定条件删除重复元素
 */

public class DeleteDuplicatesBy {
    public static <T, R> List<T> deleteDuplicatesBy(List<T> list, Function<T, R> function) {
        ObjectHelper.requireNonNull(list, "list");
        ObjectHelper.requireNonNull(function, "function");

        if (list.size() == 0) {
            return new ArrayList<>();
        } else {
            List<T> result = new ArrayList<>();
            result.add(list.get(0));//第一个元素肯定不和其他元素重复
            for (int i = 1; i < list.size(); i++) {
                boolean containsQ = false;
                for (int j = 0; j < result.size(); j++) {
                    if (function.apply(list.get(i)).equals(function.apply(result.get(j)))) {
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
