package xxl.mathematica.string;

import xxl.mathematica.ObjectHelper;

import java.util.Arrays;
import java.util.List;

/**
 * 分割字符串
 */
public class StringSplit {
  public static List<String> stringSplit(String str,List<String> splitter) {
    ObjectHelper.requireNonNull(str);
    return Arrays.asList(str.split(" "));
  }
}
