package xxl.mathematica.process;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;

public class Shell {

  private static final ProcessBuilder pb = new ProcessBuilder();

  /**
   * 执行命令，并得到应答
   *
   * @param command
   * @return
   * @throws IOException
   */
  public static byte[] exec(String command) throws IOException {
    return exec(null, command);
  }

  /**
   * 在指定目录执行命令，并得到应答
   *
   * @param dir
   * @param command
   * @return
   * @throws IOException
   */
  public static byte[] exec(File dir, String command) throws IOException {
    if (ObjectUtils.isEmpty(command)) return new byte[0];
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
