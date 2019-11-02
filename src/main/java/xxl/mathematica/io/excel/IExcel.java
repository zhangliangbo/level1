package xxl.mathematica.io.excel;

import java.util.List;

public interface IExcel {
  /**
   * poi实现
   */
  int POI = 1;
  /**
   * jxl实现
   */
  int JXL = 2;

  /**
   * 导出xls
   *
   * @param file
   * @param lists
   * @return
   */
  boolean exportXls(String file, List<Object>... lists) throws Exception;

  /**
   * 导出xlsx
   *
   * @param file
   * @param lists
   * @return
   */
  boolean exportXlsx(String file, List<Object>... lists) throws Exception;
}
