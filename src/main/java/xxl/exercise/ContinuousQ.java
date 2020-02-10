package xxl.exercise;

import xxl.mathematica.ObjectHelper;

import java.util.Collections;
import java.util.List;

public class ContinuousQ {
  /**
   * 是否连续
   *
   * @param list
   * @return
   */
  public static boolean continuousQ(List<Long> list) {
    ObjectHelper.requireNonNull(list);
    Collections.sort(list);
    for (int i = 1; i < list.size(); i++) {
      if (list.get(i) - list.get(i - 1) != 1) {
        return false;
      }
    }
    return true;
  }

}
