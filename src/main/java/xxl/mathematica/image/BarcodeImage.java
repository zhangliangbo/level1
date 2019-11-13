package xxl.mathematica.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 条形码图像
 */
public class BarcodeImage {
  /**
   * 生成二维码
   *
   * @param content
   * @param format
   * @param width
   * @param height
   * @return
   */
  public static boolean barcodeImage(String content, BarcodeFormat format, int width, int height, String outPath) {
    Map<EncodeHintType, Object> hints = new HashMap<>();
    hints.put(EncodeHintType.CHARACTER_SET, "UTF8");
    try {
      BitMatrix bitMatrix = new MultiFormatWriter().encode(content, format, width, height, hints);
      Path file = new File(outPath).toPath();
      MatrixToImageWriter.writeToPath(bitMatrix, outPath.substring(1 + outPath.lastIndexOf(".")), file);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * 生成二维码
   *
   * @param content
   * @param format
   * @param size    长或宽
   * @param outPath
   * @return
   */
  public static boolean barcodeImage(String content, BarcodeFormat format, int size, String outPath) {
    return barcodeImage(content, format, size, size, outPath);
  }

  /**
   * 生成二维码
   *
   * @param content 内容
   * @param size
   * @param outPath
   * @return
   */
  public static boolean barcodeImage(String content, int size, String outPath) {
    return barcodeImage(content, BarcodeFormat.QR_CODE, size, size, outPath);
  }
}
