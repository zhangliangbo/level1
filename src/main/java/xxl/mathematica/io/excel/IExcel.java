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
   * @param file            excel文件
   * @param withAnnotationQ 是否有注解
   * @param sheets          表格
   * @return
   */
  boolean exportExcel(String file, boolean withAnnotationQ, List<List<Object>> sheets) throws Exception;

  /**
   * 导入excel为文本
   *
   * @param file excel文件
   * @return
   * @throws Exception
   */
  List<List<String[]>> importExcel(String file) throws Exception;
}
