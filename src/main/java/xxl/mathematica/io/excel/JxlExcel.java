package xxl.mathematica.io.excel;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import xxl.mathematica.Select;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * jxl实现，可用于android
 */
final class JxlExcel implements IExcel {

  private JxlExcel() {

  }

  public static JxlExcel getInstance() {
    return Holder.jxlExcel;
  }

  @Override
  public boolean exportXls(String file, List<Object>... lists) throws Exception {
    return exportXlsx(file, lists);
  }

  @Override
  public boolean exportXlsx(String file, List<Object>... lists) throws Exception {
    return exportXlsx(file, false, lists);
  }

  @Override
  public boolean exportXls(String file, boolean withAnnotationQ, List<Object>... lists) throws Exception {
    return exportXlsx(file, withAnnotationQ, lists);
  }

  @Override
  public boolean exportXlsx(String file, boolean withAnnotationQ, List<Object>... lists) throws Exception {
    WritableWorkbook workbook = Workbook.createWorkbook(new File(file));
    for (int k = 0; k < lists.length; k++) {
      List<Object> list = lists[k];
      WritableSheet sheet = workbook.createSheet("Sheet" + (k + 1), k);
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
              for (int j = 0; j < fields.length; j++) {
                if (fields[j].isAnnotationPresent(ExcelColumnName.class)) {
                  sheet.addCell(new Label(j, i, fields[j].getAnnotation(ExcelColumnName.class).value()));
                } else {
                  if (!withAnnotationQ) {
                    sheet.addCell(new Label(j, i, "Column" + (j + 1)));
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
              for (int j = 0; j < fields.length; j++) {
                if (fields[j].isAnnotationPresent(ExcelColumnName.class) || !withAnnotationQ) {
                  if (!fields[j].isAccessible()) {
                    fields[j].setAccessible(true);
                  }
                  String value = fields[j].get(object) == null ? "" : fields[j].get(object).toString();
                  //确定单元格类型
                  Class<?> cls = fields[j].getType();
                  if (cls.isPrimitive()) {
                    if (cls == boolean.class || cls == Boolean.class) {
                      sheet.addCell(new jxl.write.Boolean(j, i, Boolean.parseBoolean(value)));
                    } else if (cls == char.class || cls == Character.class) {
                      sheet.addCell(new jxl.write.Label(j, i, value));
                    } else {
                      sheet.addCell(new jxl.write.Number(j, i, Double.parseDouble(value)));
                    }
                  } else {
                    sheet.addCell(new Label(j, i, value));
                  }
                }
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

  private static class Holder {
    static JxlExcel jxlExcel = new JxlExcel();
  }
}
