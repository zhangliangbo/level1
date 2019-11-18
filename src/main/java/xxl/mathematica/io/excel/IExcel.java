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
   * 导出excel
   *
   * @param file
   * @param withAnnotationQ
   * @param lists
   * @return
   */
  boolean exportExcel(String file, boolean withAnnotationQ, List<Object>... lists) throws Exception;
}
