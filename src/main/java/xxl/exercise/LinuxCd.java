package xxl.exercise;

import java.io.File;
import java.util.Scanner;

public class LinuxCd {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      int count = Integer.parseInt(sc.nextLine());
      File cur = new File("/");
      for (int i = 0; i < count; i++) {
        if (sc.hasNextLine()) {
          String line = sc.nextLine();
          String dir = line.substring(line.indexOf("cd") + 2).trim();
          if (dir.startsWith("/")) {
            cur = new File(dir);
          } else {
            String[] sub = dir.split("/");
            for (String s : sub) {
              if ("..".equals(s)) {
                cur = cur.getParentFile();
              } else if (".".equals(s)) {

              } else {
                cur = new File(cur, s);
              }
            }
          }
        }
      }
      System.out.println(cur.getAbsolutePath());
    }
  }
}
