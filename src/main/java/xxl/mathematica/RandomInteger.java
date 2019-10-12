package xxl.mathematica;

import xxl.mathematica.internal.RandomSingle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 伪随机整数
 */

public class RandomInteger {

  private static Random random = RandomSingle.instance();

  /**
   * 伪随机地给出 0 或 1.
   *
   * @return
   */
  public static int randomInteger() {
    return random.nextInt(2);
  }

  /**
   * 给出 {0,max} 范围内的伪随机整数
   *
   * @param max
   * @return
   */
  public static int randomInteger(int max) {
    if (max > 0) {
      return random.nextInt(max);
    } else if (max == 0) {
      return 0;
    } else {
      return -random.nextInt(-max);
    }
  }

  /**
   * 给出 {m,n} 范围内的伪随机整数.
   *
   * @param m
   * @param n
   * @return
   */
  public static int randomInteger(int m, int n) {
    if (m < n) {
      int len = n - m;
      return random.nextInt(len) + m;
    } else if (m > n) {
      int len = m - n;
      int r = random.nextInt(len) + n;
      //不包含结尾
      if (n >= 0) {
        return r - 1;
      } else {
        return r + 1;
      }
    } else {
      return m;
    }
  }

  /**
   * 给出num个 {m,n} 范围内的伪随机整数.
   *
   * @param m   包含
   * @param n   不包含
   * @param num
   * @return
   */
  public static List<Integer> randomInteger(int m, int n, int num) {
    List<Integer> result = new ArrayList<>();
    if (m < n) {
      int len = n - m;
      for (int i = 0; i < num; i++) {
        result.add(random.nextInt(len) + m);
      }
      return result;
    } else if (m > n) {
      int len = m - n;
      for (int i = 0; i < num; i++) {
        //不包含结尾
        result.add(n >= 0 ? random.nextInt(len) + n - 1 : random.nextInt(len) + n + 1);
      }
      return result;
    } else {
      for (int i = 0; i < num; i++) {
        result.add(num);
      }
      return result;
    }
  }
}
