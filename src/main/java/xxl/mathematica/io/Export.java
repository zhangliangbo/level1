package xxl.mathematica.io;

import xxl.mathematica.io.excel.AbsExcel;
import xxl.mathematica.io.excel.IExcel;

import java.util.List;

/**
 * 导出
 */
public class Export {

  /**
   * 默认导出所有字段，包括不带{@link xxl.mathematica.io.excel.ExcelColumnName}标注的
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(int method, String file, List<Object>... list) throws Exception {
    return exportExcel(method, file, false, list);
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
  public static boolean exportExcel(int method, String file, boolean withAnnotationQ, List<Object>... list) throws Exception {
    return AbsExcel.getExcelImpl(method).exportExcel(file, withAnnotationQ, list);
  }

  /**
   * 默认jxl，支持android
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(String file, List<Object>... list) throws Exception {
    return exportExcel(IExcel.JXL, file, list);
  }

}
