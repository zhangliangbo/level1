package xxl.mathematica.string;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串匹配
 */
public class StringCases {
  public static List<String> stringCases(String source, String regex) {
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(source);
    return Arrays.asList(pattern.split(source));
  }
}
