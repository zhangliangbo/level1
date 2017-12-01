package com.mcivicm.mathematica;

import com.mcivicm.mathematica.function.Function;

import java.io.File;

/**
 * 目录名称
 */

public class DirectoryName {
    /**
     * 从指定的文件中提取目录名称.
     *
     * @param file
     * @return
     */
    public static String directoryName(String file) {
        return directoryName(file, 1);
    }

    /**
     * 应用 n 次
     *
     * @param file
     * @param n
     * @return
     */
    public static String directoryName(String file, int n) {
        ObjectHelper.requireNonNull(file, "file");
        ObjectHelper.verifyPositive(n, "n");
        File result = Nest.nest(new Function<File, File>() {
            @Override
            public File apply(File file) {
                if (file == null) {
                    return null;
                } else {
                    return file.getParentFile();
                }
            }
        }, new File(file), n);
        return result == null ? "" : result.getAbsolutePath();//取绝对路径
    }
}
