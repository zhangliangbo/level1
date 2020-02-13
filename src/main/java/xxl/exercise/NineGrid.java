package xxl.exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NineGrid {
  public static void main(String[] args) {
    List<int[]> ans = compute1();
    for (int[] t : ans) {
      System.out.println(Arrays.toString(t));
    }
  }

  private static List<int[]> compute() {

    List<int[]> list = new ArrayList<>();

    int[] a = new int[9];
    a[2] = 9;
    for (int i = 0; i <= 4; i++) {
      a[0] = i;
      a[1] = 13 - i; // ① a[0] + a[1] -  9   = 4

      for (int j = 0; j <= 5; j++) {
        a[5] = j;
        a[8] = 5 - j; // ⑥ 9   - a[5] - a[8] = 4

        for (int k = 1; k <= 9; k++) {
          a[4] = k;
          a[3] = 4 + a[4] * a[5]; // ② nb[3] - nb[4] * nb[5] = 4

          float temp = (a[1] - 4) / (float) a[4]; // ⑤ nb[1] - nb[4] * nb[7] = 4
          // 去小数
          if (a[1] - a[4] * temp != 4f) {
            continue;
          }
          a[7] = (int) temp;

          a[6] = 4 + a[8] - a[7]; // ③ nb[6] + nb[7] - nb[8] = 4
          if (a[6] != 0) {
            // 去小数
            if (a[0] + a[3] * 1f / a[6] == 4f) { // ④ nb[0] + nb[3] / nb[6] = 4
              int[] result = new int[a.length];
              System.arraycopy(a, 0, result, 0, a.length);
              list.add(result);
            }
          }
        }
      }
    }
    return list;
  }

  /**
   * 0 1 2
   * 3 4 5
   * 6 7 8
   */
  private static List<int[]> compute1() {
    List<int[]> res = new ArrayList<>();
    //8个变量，6个方程，需要遍历才行
    List<Integer> a6 = Arrays.asList(1, 2);
    List<Integer> a5 = Arrays.asList(1, 2, 4);
    for (int t6 : a6) {
      int[] a = new int[9];
      a[2] = 9;
      a[6] = t6;
      a5.remove(a[6]);
      for (int t5 : a5) {
        a[5] = t5;
        a[8] = 5 - a[5];
        a[7] = 4 + a[8] - a[6];
        //剩下四个等式
//        a[0] = 13 - a[1];
//        a[1] = 4 / a[7] + a[4];
//        a[4] = a[3] - 4 / a[5];
//        a[3] = 4 * a[6] - a[0];
        a[0] = 13 - (4 / a[7] + 4 * a[6] - a[0] - 4 / a[5]);
        a[1] = 13 - a[0];
        a[3] = 4 * a[6] - a[0];
        a[4] = a[3] - 4 / a[5];
      }
      Arrays.sort(a);
      boolean continuousQ = true;
      for (int i = 1; i < a.length; i++) {
        if (a[i] - a[i - 1] != 1) {
          continuousQ = false;
          break;
        }
      }
      if (continuousQ) {
        res.add(a);
      }
    }
    return res;
  }
}
