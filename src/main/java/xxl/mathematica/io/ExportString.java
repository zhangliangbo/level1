package xxl.mathematica.io;

import xxl.mathematica.single.GsonSingle;

/**
 * 导出字符串
 */
public class ExportString {
  /**
   * 导出json字符串
   *
   * @param object
   * @return
   */
  public static String exportStringJson(Object object) {
    return GsonSingle.instance().toJson(object);
  }
}
