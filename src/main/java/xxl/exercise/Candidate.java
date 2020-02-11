package xxl.exercise;

import java.util.*;

/**
 * 候选人算法
 */
public class Candidate {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      String[] candidates = line.split(" +");
      Map<String, Integer> map = new HashMap<>();
      for (String c : candidates) {
        if (map.containsKey(c)) {
          map.put(c, map.get(c) + 1);
        } else {
          map.put(c, 1);
        }
      }
      List<Map.Entry<String, Integer>> entries = new ArrayList<>(map.entrySet());
      Map.Entry<String, Integer> max = entries.get(0);
      for (int i = 1; i < entries.size(); ++i) {
        if (entries.get(i).getValue() > max.getValue()) {
          max = entries.get(i);
        }
      }
      System.out.println(max.getKey() + ":" + max.getValue());
    }
  }
}

