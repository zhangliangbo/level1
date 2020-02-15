package xxl.exercise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LinuxCd {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      int count = Integer.parseInt(sc.nextLine());
      List<String> cur = new ArrayList<>();
      for (int i = 0; i < count; i++) {
        if (sc.hasNextLine()) {
          String line = sc.nextLine();
          String dir = line.substring(line.indexOf("cd") + 2).trim();
          String[] sub = dir.split("/");
          if (dir.startsWith("/")) {
            cur.clear();
            cur.addAll(Arrays.asList(sub));
          } else {
            for (String s : sub) {
              if ("..".equals(s)) {
                cur.remove(cur.size() - 1);
              } else if (".".equals(s)) {

              } else {
                cur.add(s);
              }
            }
          }
        }
      }
      StringBuilder sb = new StringBuilder();
      for (String s : cur) {
        sb.append("/").append(s);
      }
      System.out.println(sb.toString());
    }
  }
}
