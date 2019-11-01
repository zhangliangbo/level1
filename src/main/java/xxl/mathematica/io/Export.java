package xxl.mathematica.io;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xxl.mathematica.ObjectHelper;
import xxl.mathematica.annotation.ExcelColumnName;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 导出
 */
public class Export {
  /**
   * 导出Xls
   *
   * @param list
   */
  public static boolean exportXls(String file, List<Object> list) throws Exception {
    return exportXlsx(file, list);
  }

  /**
   * 导出xlsx
   *
   * @param list
   */
  public static boolean exportXlsx(String file, List<Object> list) throws Exception {
    ObjectHelper.requireNonNull(file, list);
    // 创建新的Excel 工作簿
    XSSFWorkbook workbook = new XSSFWorkbook();
    // 在Excel工作簿中建一工作表，其名为缺省值
    XSSFSheet sheet = workbook.createSheet();
    //添加数据
    if (list.size() > 0) {
      for (int i = 0; i < list.size() + 1; i++) {
        if (i == 0) {
          Object object = list.get(0);
          if (object != null) {
            Field[] fields = object.getClass().getDeclaredFields();
            XSSFRow row = sheet.createRow(0);
            for (int j = 0; j < fields.length; j++) {
              XSSFCell cell = row.createCell(j);
              cell.setCellType(CellType.STRING);
              if (fields[j].isAnnotationPresent(ExcelColumnName.class)) {
                cell.setCellValue(fields[j].getAnnotation(ExcelColumnName.class).value());
              } else {
                cell.setCellValue("Column" + (j + 1));
              }
            }
          }
        } else {
          Object object = list.get(i - 1);
          if (object != null) {
            Field[] fields = object.getClass().getDeclaredFields();
            XSSFRow row = sheet.createRow(i);
            for (int j = 0; j < fields.length; j++) {
              XSSFCell cell = row.createCell(j);
              if (!fields[j].isAccessible()) {
                fields[j].setAccessible(true);
              }
              Class<?> cls = fields[j].getType();
              if (cls.isPrimitive()) {
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.valueOf(fields[j].get(object).toString()));
              } else {
                cell.setCellType(CellType.STRING);
                cell.setCellValue((String) fields[j].get(object));
              }
            }
          }
        }
      }
    }
    // 新建一输出文件流
    FileOutputStream fos = new FileOutputStream(file);
    // 把相应的Excel 工作簿存盘
    workbook.write(fos);
    fos.flush();
    // 操作结束，关闭文件
    fos.close();
    return true;
  }
}
