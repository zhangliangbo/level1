package xxl.mathematica.external;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;

public class External {

  private static final ProcessBuilder pb = new ProcessBuilder();

  /**
   * 执行命令，并返回执行状态码
   *
   * @param command
   * @return
   * @throws IOException
   */
  public static int run(String command) throws IOException, InterruptedException {
    return run(null, command);
  }

  /**
   * 执行命令，并得到状态码
   *
   * @param dir
   * @param command
   * @return
   * @throws IOException
   */
  public static int run(File dir, String command) throws IOException, InterruptedException {
    if (ObjectUtils.isEmpty(command)) throw new IllegalArgumentException("command is empty");
    String[] commands = command.split(" ");
    pb.directory(dir);
    pb.command(commands);
    Process sub = pb.start();
    sub.waitFor();
    return sub.exitValue();
  }

  /**
   * 执行命令，并得到应答
   *
   * @param command
   * @return
   * @throws IOException
   */
  public static byte[] runProcess(String command) throws IOException {
    return runProcess(null, command);
  }

  /**
   * 在指定目录执行命令，并得到应答
   *
   * @param dir
   * @param command
   * @return
   * @throws IOException
   */
  public static byte[] runProcess(File dir, String command) throws IOException {
    if (ObjectUtils.isEmpty(command)) throw new IllegalArgumentException("command is empty");
    String[] commands = command.split(" ");
    pb.directory(dir);
    pb.command(commands);
    Process sub = pb.start();
    byte[] in = IOUtils.toByteArray(sub.getInputStream());
    byte[] error = IOUtils.toByteArray(sub.getErrorStream());
    if (ArrayUtils.isEmpty(in)) {
      return error;
    } else {
      return in;
    }
  }

}
