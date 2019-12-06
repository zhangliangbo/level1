package xxl.mathematica;

import java.io.File;

/**
 * 文件基本名
 */

public class FileBaseName {
    /**
     * 给出无扩展名的一个文件的基本名称.
     *
     * @param file
     * @return
     */
    public static String fileBaseName(String file) {
        ObjectHelper.requireNonNull(file);
        if (file.length() == 0) {
            return null;//空则返回空
        } else {
            //分割，取得最后的文件名
            String fileName = file;
            int separator = file.lastIndexOf(File.separator);
            if (separator != -1) {
                fileName = file.substring(separator + 1);
            }
            int dot = fileName.lastIndexOf('.');
            if (dot >= 0 && dot < file.length()) {
                return fileName.substring(0, dot);
            } else {
                return fileName;
            }
        }

    }
}
