package xxl.mathematica.io;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import xxl.mathematica.io.excel.AbsExcel;
import xxl.mathematica.io.excel.IExcel;

import java.io.FileOutputStream;
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

    /**
     * 导出word
     *
     * @param file
     * @param contents
     * @return
     */
    public static boolean exportWord(String file, List<Object> contents) throws Exception {
        XWPFDocument doc = new XWPFDocument();// 创建Word文件
        if (contents != null) {
            for (Object object : contents) {
                if (object instanceof String) {
                    XWPFParagraph p = doc.createParagraph();// 新建一个段落
                    XWPFRun r = p.createRun();//新建一个文本
                    r.setText((String) object);
                } else if (object instanceof String[]) {
                    String[] one = (String[]) object;
                    if (one.length > 0) {
                        XWPFTable table = doc.createTable(1, one.length);//创建一个表格
                        for (int col = 0; col < one.length; col++) {
                            table.getRow(0).getCell(col).setText(one[col]);
                        }
                    }
                } else if (object instanceof String[][]) {
                    String[][] array = (String[][]) object;
                    if (array.length > 0) {
                        String[] head = array[0];
                        if (head.length > 0) {
                            XWPFTable table = doc.createTable(array.length, head.length);//创建一个表格
                            for (int row = 0; row < array.length; row++) {
                                for (int col = 0; col < head.length; col++) {
                                    table.getRow(row).getCell(col).setText(array[row][col]);
                                }
                            }
                        }
                    }
                }
            }
        }
        FileOutputStream out = new FileOutputStream(file);
        doc.write(out);
        out.close();
        return true;
    }

}
