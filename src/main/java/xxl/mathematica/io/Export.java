package xxl.mathematica.io;

import xxl.mathematica.io.excel.AbsExcel;
import xxl.mathematica.io.excel.IExcel;

import java.util.List;

/**
 * 导出
 */
public class Export {

  /**
   * 默认jxl，支持android
   *
   * @param list
   */
  public static boolean exportXls(String file, List<Object>... list) throws Exception {
    return exportXlsx(file, list);
  }

  /**
   * 默认导出所有字段，无论有没有注解
   *
   * @param list
   */
  public static boolean exportXls(int method, String file, List<Object>... list) throws Exception {
    return exportXlsx(method, file, list);
  }

  /**
   * 导出xls
   *
   * @param method
   * @param file
   * @param withAnnotationQ
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXls(int method, String file, boolean withAnnotationQ, List<Object>... list) throws Exception {
    return exportXlsx(method, file, withAnnotationQ, list);
  }

  /**
   * 默认jxl，支持android
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXlsx(String file, List<Object>... list) throws Exception {
    return AbsExcel.getExcelImpl(IExcel.JXL).exportXlsx(file, list);
  }

  /**
   * 默认导出所有字段，包括不带标注的
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXlsx(int method, String file, List<Object>... list) throws Exception {
    return exportXlsx(method, file, false, list);
  }

  /**
   * 导出xlsx
   *
   * @param method
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXlsx(int method, String file, boolean withAnnotationQ, List<Object>... list) throws Exception {
    return AbsExcel.getExcelImpl(method).exportXlsx(file, withAnnotationQ, list);
  }

}
