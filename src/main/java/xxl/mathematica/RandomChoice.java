package xxl.mathematica;

import xxl.mathematica.single.RandomSingle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机选择
 */
public class RandomChoice {

  private static Random random = RandomSingle.instance();

  public static <T> List<T> randomChoice(List<T> list, int n) {
    ObjectHelper.requireNonNull(list);
    ObjectHelper.requireNonNegative(n);
    List<T> res = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      res.add(list.get(random.nextInt(list.size())));
    }
    return res;
  }

  public static <T> T randomChoice(List<T> list) {
    ObjectHelper.requireNonNull(list);
    return list.get(random.nextInt(list.size()));
  }
}
