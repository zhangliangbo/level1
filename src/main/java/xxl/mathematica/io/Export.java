package xxl.mathematica.io;

import xxl.mathematica.io.excel.AbsExcel;
import xxl.mathematica.io.excel.IExcel;

import java.util.List;

/**
 * 导出
 */
public class Export {

  /**
   * 导出xls，默认jxl，支持android
   *
   * @param list
   */
  public static boolean exportXls(String file, List<Object>... list) throws Exception {
    return exportXlsx(file, list);
  }

  /**
   * 导出xls
   *
   * @param list
   */
  public static boolean exportXls(int method, String file, List<Object>... list) throws Exception {
    return exportXlsx(method, file, list);
  }

  /**
   * 导出xlsx
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
   * 导出xlsx
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXlsx(int method, String file, List<Object>... list) throws Exception {
    return AbsExcel.getExcelImpl(method).exportXlsx(file, list);
  }

}
