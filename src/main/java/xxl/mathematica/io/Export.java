package xxl.mathematica.io;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xxl.mathematica.FileBaseName;
import xxl.mathematica.ObjectHelper;
import xxl.mathematica.annotation.ExcelColumnName;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
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
  public static boolean exportXls(String file, List<Object> list) throws Exception {
    return exportXlsx(file, list);
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
  public static boolean exportXlsWithJxl(String file, List<Object> list) throws Exception {
    return exportXlsxWithJxl(file, list);
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
              //确定单元格类型
              Class<?> cls = fields[j].getType();
              if (cls.isPrimitive()) {
                if (cls == boolean.class || cls == Boolean.class) {
                  cell.setCellType(CellType.BOOLEAN);
                  cell.setCellValue(Boolean.valueOf(fields[j].get(object).toString()));
                } else if (cls == char.class || cls == Character.class) {
                  cell.setCellType(CellType.STRING);
                  cell.setCellValue((String) fields[j].get(object));
                } else {
                  cell.setCellType(CellType.NUMERIC);
                  cell.setCellValue(Double.valueOf(fields[j].get(object).toString()));
                }
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

  public static boolean exportXlsxWithJxl(String file, List<Object> list) throws Exception {
    WritableWorkbook workbook = Workbook.createWorkbook(new File(file));
    WritableSheet sheet = workbook.createSheet(FileBaseName.fileBaseName(file), 0);
    if (list.size() > 0) {
      for (int i = 0; i < list.size() + 1; i++) {
        if (i == 0) {
          Object object = list.get(0);
          if (object != null) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
              if (fields[j].isAnnotationPresent(ExcelColumnName.class)) {
                sheet.addCell(new Label(j, i, fields[j].getAnnotation(ExcelColumnName.class).value()));
              } else {
                sheet.addCell(new Label(j, i, "Column" + (j + 1)));
              }
            }
          }
        } else {
          Object object = list.get(i - 1);
          if (object != null) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int j = 0; j < fields.length; j++) {
              if (!fields[j].isAccessible()) {
                fields[j].setAccessible(true);
              }
              //确定单元格类型
              Class<?> cls = fields[j].getType();
              if (cls.isPrimitive()) {
                if (cls == boolean.class || cls == Boolean.class) {
                  sheet.addCell(new jxl.write.Boolean(j, i, Boolean.valueOf(fields[j].get(object).toString())));
                } else if (cls == char.class || cls == Character.class) {
                  sheet.addCell(new Label(j, i, fields[j].get(object).toString()));
                } else {
                  sheet.addCell(new Number(j, i, Double.valueOf(fields[j].get(object).toString())));
                }
              } else {
                sheet.addCell(new Label(j, i, fields[j].get(object).toString()));
              }
            }
          }
        }
      }
    }
    workbook.write();
    workbook.close();
    return true;
  }
}
