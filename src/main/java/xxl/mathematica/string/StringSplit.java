package xxl.mathematica.string;

import xxl.mathematica.ObjectHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 分割字符串
 */
public class StringSplit {
  /**
   * 按照给定的字符串列表分割字符
   *
   * @param str
   * @param splitter
   * @return
   */
  public static List<String> stringSplit(String str, List<String> splitter) {
    ObjectHelper.requireNonNull(str, splitter);
    String split = StringRiffle.stringRiffle(splitter, "|");
    String[] a = str.split(split);
    return Arrays.asList(a);
  }

  /**
   * 默认空格分隔
   *
   * @param str
   * @return
   */
  public static List<String> stringSplit(String str) {
    return stringSplit(str, Collections.singletonList(" "));
  }
}
