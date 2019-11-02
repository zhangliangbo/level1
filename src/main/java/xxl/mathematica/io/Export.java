package xxl.mathematica.io;

import xxl.mathematica.io.excel.IExcel;
import xxl.mathematica.io.excel.JxlExcel;
import xxl.mathematica.io.excel.PoiExcel;

import java.util.List;

/**
 * 导出
 */
public class Export {

  /**
   * 导出xls
   *
   * @param list
   */
  public static boolean exportXls(int method, String file, List<Object>... list) throws Exception {
    return exportXlsx(method, file, list);
  }

  /**
   * 导出xls
   * 使用【jxl】库
   *
   * @param file
   * @param list
   * @return
   * @throws Exception
   */
  public static boolean exportXlsx(int method, String file, List<Object>... list) throws Exception {
    return getExcelImpl(method).exportXlsx(file, list);
  }

  private static IExcel getExcelImpl(int method) {
    switch (method) {
      case IExcel.POI:
        return PoiExcel.getInstance();
      case IExcel.JXL:
        return JxlExcel.getInstance();
      default:
        throw new IllegalArgumentException("no such implementation");
    }
  }

}
