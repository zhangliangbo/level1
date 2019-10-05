package xxl.ffmpeg;

import xxl.mathematica.external.External;

public class FFmpeg implements Iffmpeg {

  private static String exec;

  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      exec = FFmpeg.class.getResource("ffmpeg.exe").getFile();
    } else {
      throw new IllegalStateException("无法找到ffmpeg可执行文件");
    }
  }

  @Override
  public String getLicense() {
    return External.runProcess(exec + "-h");
  }

}
