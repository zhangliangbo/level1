package xxl.exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NineGrid {
  public static void main(String[] args) {
    List<int[]> ans = compute();
    for (int[] t : ans) {
      System.out.println(Arrays.toString(t));
    }
  }

  /**
   * 0 1 2
   * 3 4 5
   * 6 7 8
   */
  private static List<int[]> compute() {
    List<int[]> res = new ArrayList<>();
    //8个变量，6个方程，需要遍历才行
    //a5要能被4整除
    List<Integer> a5 = new ArrayList<>();
    a5.add(1);
    a5.add(2);
    a5.add(4);
    for (int t5 : a5) {
      //a7也要能被4整除
      List<Integer> a7 = new ArrayList<>();
      a7.add(1);
      a7.add(2);
      a7.add(4);
      //a7和a5互斥
      a7.remove(Integer.valueOf(t5));
      //剩余的数
      List<Integer> rest = new ArrayList<>();
      for (int i = 0; i < 9; i++) {
        rest.add(i + 1);
      }
      for (int t7 : a7) {
        int[] a = new int[9];
        a[2] = 9;
        rest.remove(Integer.valueOf(a[2]));
        a[5] = t5;
        rest.remove(Integer.valueOf(a[5]));
        a[7] = t7;
        rest.remove(Integer.valueOf(a[7]));
        a[8] = 5 - a[5];//a[5]+a[8]=5
        rest.remove(Integer.valueOf(a[8]));
        a[6] = 4 + a[8] - a[7];//a[6]+a[7]-a[8]=4
        rest.remove(Integer.valueOf(a[6]));
        //剩下四个等式，遍历剩余的数
        for (int tr : rest) {
          a[0] = tr;
          a[1] = 13 - a[0];//a[0]+a[1]=13
          a[3] = 4 * a[6] - a[0];//a[0]+a[3]=4*a[6]
          a[4] = a[1] - 4 / a[7];//a[1]-a[4]=4/a[7]
          //最后一个等式校验//a[3]-a[4]=4/a[5]
          if (a[3] - a[4] == 4 / a[5]) {
            //判断有效性
            List<Integer> valid = new ArrayList<>();
            for (int t : a) {
              valid.add(t);
            }
            Collections.sort(valid);
            boolean natureQ = true;
            for (int i = 1; i < valid.size(); i++) {
              if (valid.get(i) - valid.get(i - 1) != 1) {
                natureQ = false;
                break;
              }
            }
            if (natureQ) {
              int[] bingo = new int[9];
              //防止污染
              System.arraycopy(a, 0, bingo, 0, a.length);
              res.add(bingo);
            }
          }
        }
      }
    }
    return res;
  }
}
