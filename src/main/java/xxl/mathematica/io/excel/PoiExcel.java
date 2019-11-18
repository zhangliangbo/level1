package xxl.mathematica.io.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import xxl.mathematica.ObjectHelper;
import xxl.mathematica.Select;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * poi实现
 */
final class PoiExcel implements IExcel {

  private PoiExcel() {

  }

  public static PoiExcel getInstance() {
    return Holder.poiExcel;
  }

  @Override
  public boolean exportExcel(String file, boolean withAnnotationQ, List<List<Object>> sheets) throws Exception {
    ObjectHelper.requireNonNull(file, sheets);
    // 创建新的Excel 工作簿
    XSSFWorkbook workbook = new XSSFWorkbook();
    for (int k = 0; k < sheets.size(); k++) {
      List<Object> list = sheets.get(k);
      // 在Excel工作簿中建一工作表，其名为缺省值
      XSSFSheet sheet = workbook.createSheet("Sheet" + (k + 1));
      //添加数据
      if (list.size() > 0) {
        for (int i = 0; i < list.size() + 1; i++) {
          if (i == 0) {
            Object object = list.get(0);
            if (object != null) {
              Field[] fields = object.getClass().getDeclaredFields();
              if (withAnnotationQ) {
                fields = Select.select(Arrays.asList(fields), t -> t.isAnnotationPresent(ExcelColumnName.class)).toArray(new Field[0]);
              }
              Arrays.sort(fields, ExcelNameComparator.getInstance());
              XSSFRow row = sheet.createRow(0);
              for (int j = 0; j < fields.length; j++) {
                if (fields[j].isAnnotationPresent(ExcelColumnName.class)) {
                  XSSFCell cell = row.createCell(j);
                  cell.setCellType(CellType.STRING);
                  cell.setCellValue(fields[j].getAnnotation(ExcelColumnName.class).value());
                } else {
                  if (!withAnnotationQ) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue("Column" + (j + 1));
                  }
                }
              }
            }
          } else {
            Object object = list.get(i - 1);
            if (object != null) {
              Field[] fields = object.getClass().getDeclaredFields();
              if (withAnnotationQ) {
                fields = Select.select(Arrays.asList(fields), t -> t.isAnnotationPresent(ExcelColumnName.class)).toArray(new Field[0]);
              }
              Arrays.sort(fields, ExcelNameComparator.getInstance());
              XSSFRow row = sheet.createRow(i);
              for (int j = 0; j < fields.length; j++) {
                if (fields[j].isAnnotationPresent(ExcelColumnName.class) || !withAnnotationQ) {
                  XSSFCell cell = row.createCell(j);
                  if (!fields[j].isAccessible()) {
                    fields[j].setAccessible(true);
                  }
                  String value = fields[j].get(object) == null ? "" : fields[j].get(object).toString();
                  //确定单元格类型
                  Class<?> cls = fields[j].getType();
                  if (cls.isPrimitive()) {
                    if (cls == boolean.class || cls == Boolean.class) {
                      cell.setCellType(CellType.BOOLEAN);
                      cell.setCellValue(Boolean.parseBoolean(value));
                    } else if (cls == char.class || cls == Character.class) {
                      cell.setCellType(CellType.STRING);
                      cell.setCellValue(value);
                    } else {
                      cell.setCellType(CellType.NUMERIC);
                      cell.setCellValue(Double.parseDouble(value));
                    }
                  } else {
                    cell.setCellType(CellType.STRING);
                    cell.setCellValue(value);
                  }
                }
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

  @Override
  public List<List<String[]>> importExcel(String file) throws Exception {
    FileInputStream fis = new FileInputStream(file);
    HSSFWorkbook wb = new HSSFWorkbook(fis);
    Iterator<Sheet> sheetIterator = wb.sheetIterator();
    List<List<String[]>> sheetList = new ArrayList<>();
    while (sheetIterator.hasNext()) {
      Sheet sheet = sheetIterator.next();
      Iterator<Row> rowIterator = sheet.rowIterator();
      List<String[]> rowList = new ArrayList<>();
      while (rowIterator.hasNext()) {
        Row row = rowIterator.next();
        Iterator<Cell> cellIterator = row.cellIterator();
        List<String> columnList = new ArrayList<>();
        while (cellIterator.hasNext()) {
          Cell cell = cellIterator.next();
          switch (cell.getCellType()) {
            case STRING:
              columnList.add(cell.getStringCellValue());
              break;
            case BOOLEAN:
              columnList.add(String.valueOf(cell.getBooleanCellValue()));
              break;
            case NUMERIC:
              columnList.add(String.valueOf(cell.getNumericCellValue()));
              break;
          }
        }
        rowList.add(columnList.toArray(new String[0]));
      }
      sheetList.add(rowList);
    }
    return sheetList;
  }

  private static class Holder {
    static PoiExcel poiExcel = new PoiExcel();
  }
}
