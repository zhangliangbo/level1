package xxl.exercise;

import java.util.Scanner;

public class LongestPalindromic {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      int len = line.length();
      int max = len;
      String res = null;
      while (max > 0) {
        //从最长的开始找
        for (int i = 0; i < len - max + 1; i++) {
          String sub = line.substring(i, i + max);
          //判断子串是否有效
          char[] subChars = sub.toCharArray();
          boolean isQ = true;
          for (int j = 0; j < subChars.length / 2; j++) {
            if (subChars[j] != subChars[subChars.length - 1 - j]) {
              isQ = false;
              break;
            }
          }
          //有效则退出
          if (isQ) {
            res = sub;
            break;
          }
        }
        if (res != null) {
          break;
        } else {
          --max;
        }
      }
      System.out.println(res);
    }
  }
}
