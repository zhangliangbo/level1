package xxl.mathematica.io;

import org.apache.commons.io.IOUtils;
import xxl.codec.Charsets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 导入
 */
public class Import {
  /**
   * 以文本形式导入
   *
   * @param file
   * @return
   */
  public static String importText(File file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      return IOUtils.toString(fis, Charsets.UTF_8);
    } catch (IOException e) {
      return null;
    }
  }
}
