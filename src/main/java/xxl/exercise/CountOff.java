package xxl.exercise;

import java.util.Scanner;

/**
 * 报数
 */
public class CountOff {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      String[] nums = line.split(" +");
      int m = Integer.parseInt(nums[0]);
      int n = Integer.parseInt(nums[1]);
      int k = Integer.parseInt(nums[2]);
      if (m == 0 && n == 0 && k == 0) {
        break;
      } else {
        Counter counter = new Counter(m, n, k, 7);
        System.out.println(counter.getCount());
      }
    }
  }

  static class Counter {
    int k = 0;
    int m = 0;
    int n = 0;
    int special = 0;

    public Counter(int n, int m, int k, int special) {
      this.n = n;
      this.m = m;
      this.k = k;
      this.special = special;
    }

    public int getCount() {
      int count = 0;
      int current = 0;
      int times = 0;
      int direction = 1;
      while (true) {
        //计数
        ++count;
        //编号
        if (current == 0 || current == 1) {
          direction = 1;
        } else if (current == n) {
          direction = -1;
        }
        current += direction;
        //次数
        if (current == m && (String.valueOf(count).contains(String.valueOf(special)) || count % special == 0)) {
          ++times;
          if (times == k) {
            break;
          }
        }
      }
      return count;
    }
  }
}
