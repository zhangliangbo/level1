package xxl.mathematica;

/**
 * 文件扩展名
 */

public class FileExtension {
    /**
     * 给出一个文件的扩展名.
     *
     * @param file
     * @return
     */
    public static String fileExtension(String file) {
        ObjectHelper.requireNonNull(file);
        if (file.length() == 0) {
            return null;
        } else {
            int dot = file.lastIndexOf('.');
            if (dot > -0 && dot < file.length()) {
                return file.substring(dot + 1);
            } else {
                return null;//找不到.则无法给出文件名
            }
        }
    }
}
