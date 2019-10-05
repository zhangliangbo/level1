package xxl.ffmpeg;

public class FFmpeg implements Iffmpeg {

  public static String exec = null;

  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("Windows")) {
      exec = FFmpeg.class.getResource("ffmpeg.exe").getFile();
    } else {
      throw new IllegalStateException("无法找到ffmpeg可执行文件");
    }
  }

  private FFmpeg() {

  }

  public FFmpeg getInstance() {
    return Holder.ffmpeg;
  }

  @Override
  public String getLicense() {
    return
  }

  private static class Holder {
    private static FFmpeg ffmpeg = new FFmpeg();
  }
}
