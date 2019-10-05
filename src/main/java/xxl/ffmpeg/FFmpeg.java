package xxl.ffmpeg;

import xxl.mathematica.external.External;

import java.io.IOException;

public class FFmpeg implements Iffmpeg {

  private static String exec;

  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      exec = FFmpeg.class.getClassLoader().getResource("ffmpeg/windows/ffmpeg.exe").getFile();
    } else if (os.startsWith("Linux")) {
      String arch = System.getProperty("os.arch");
      if (arch.startsWith("armv8") || arch.startsWith("arm64")) {
        exec = FFmpeg.class.getClassLoader().getResource("ffmpeg/arm64/ffmpeg").getFile();
      } else {
        throw new IllegalStateException("未知架构");
      }
    } else {
      throw new IllegalStateException("未知系统");
    }
  }

  private FFmpeg() {

  }

  /**
   * 获取单例
   *
   * @return
   */
  public static FFmpeg getInstance() {
    return Holder.ffmpeg;
  }

  @Override
  public String getLicense() {
    try {
      return new String(External.runProcess(exec + " -L"));
    } catch (IOException e) {
      return "";
    }
  }

  static class Holder {
    static FFmpeg ffmpeg = new FFmpeg();
  }

}
