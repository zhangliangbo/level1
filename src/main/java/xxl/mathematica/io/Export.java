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
   * @param sheets
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(int method, String file, List<List<Object>> sheets) throws Exception {
    return exportExcel(method, file, false, sheets);
  }

  /**
   * 导出xlsx
   *
   * @param method
   * @param file
   * @param sheets
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(int method, String file, boolean withAnnotationQ, List<List<Object>> sheets) throws Exception {
    return AbsExcel.getExcelImpl(method).exportExcel(file, withAnnotationQ, sheets);
  }

  /**
   * 默认jxl，支持android
   *
   * @param file
   * @param withAnnotationQ
   * @param sheets
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(String file, boolean withAnnotationQ, List<List<Object>> sheets) throws Exception {
    return exportExcel(IExcel.JXL, file, withAnnotationQ, sheets);
  }

  /**
   * 默认jxl，支持android
   *
   * @param file
   * @param sheets
   * @return
   * @throws Exception
   */
  public static boolean exportExcel(String file, List<List<Object>> sheets) throws Exception {
    return exportExcel(IExcel.JXL, file, sheets);
  }

}
