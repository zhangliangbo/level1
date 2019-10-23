package xxl.mathematica;

import xxl.mathematica.function.BiPredicate;

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
    return deleteDuplicates(list, new BiPredicate<T, T>() {
      @Override
      public boolean test(T t, T t2) {
        return t.equals(t2);
      }
    });
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
    ObjectHelper.requireNonNull(list, test);
    if (list.size() == 0) {
      return new ArrayList<>();
    } else if (list.size() == 1) {//一个元素不存在重复值
      return new ArrayList<>(list);
    } else {
      List<T> result = new ArrayList<>();
      result.add(list.get(0));
      for (int i = 1; i < list.size(); i++) {
        boolean containsQ = false;
        for (T t : result) {
          //从左到右分别是列表【前面的值】和列表【当前值】
          if (test.test(t, list.get(i))) {
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
