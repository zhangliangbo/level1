package xxl.exercise;

import java.util.Scanner;

/**
 * 士兵分子弹
 */
public class DispatchBullet {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      String[] bullets = line.split(" +");
      Soldier[] soldiers = new Soldier[bullets.length];
      if (soldiers.length > 0) {
        for (int i = 0; i < bullets.length; ++i) {
          soldiers[i] = new Soldier(i, Integer.parseInt(bullets[i]));
        }
        int times = 0;
        while (true) {
          ++times;
          //变成偶数
          for (Soldier soldier : soldiers) {
            if (soldier.bullet % 2 != 0) {
              soldier.bullet += 1;
            }
          }
          //计算一半的子弹
          for (Soldier s : soldiers) {
            s.half = s.bullet / 2;
          }
          //给下一个战士
          for (int i = 0; i < soldiers.length; ++i) {
            if (i == soldiers.length - 1) {
              soldiers[0].bullet = soldiers[0].half + soldiers[i].half;
            } else {
              soldiers[i + 1].bullet = soldiers[i + 1].half + soldiers[i].half;
            }
          }
          //判断相等
          boolean equalQ = true;
          for (int i = 1; i < soldiers.length; ++i) {
            if (soldiers[i].bullet != soldiers[0].bullet) {
              equalQ = false;
              break;
            }
          }
          if (equalQ) {
            break;
          }
        }
        System.out.println(times + ":" + soldiers[0].bullet);
      }
    }
  }

  static class Soldier {
    int bullet;
    int half;
    int index;

    public Soldier(int index, int bullet) {
      this.index = index;
      this.bullet = bullet;
    }
  }
}
