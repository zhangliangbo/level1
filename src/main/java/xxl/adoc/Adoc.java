package xxl.adoc;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;
import xxl.mathematica.FileBaseName;
import xxl.mathematica.Rule;
import xxl.mathematica.external.External;
import xxl.mathematica.string.StringSplit;
import xxl.os.OS;

import java.io.File;
import java.util.List;

/**
 * adoc文档
 */
public class Adoc {
    /**
     * 导出格式
     */
    public enum Output {
        html,
        xml,
        pdf,
        epub3
    }

    /**
     * 转换adoc文档
     *
     * @return
     */
    public static String convertFile(String adoc, Output output, String dest) {
        File adocFile = new File(adoc);
        if (!adocFile.exists()) {
            return null;
        }
        File destDir = dest == null ? adocFile.getParentFile() : new File(dest);
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                return null;
            }
        }
        String backend;
        String format;
        switch (output) {
            case pdf:
                backend = "pdf";
                format = "pdf";
                break;
            case epub3:
                backend = "epub3";
                format = "epub3";
                break;
            case xml:
                backend = "docbook";
                format = "xml";
                break;
            default:
                backend = "html";
                format = "html";
                break;
        }
        if (output == Output.html || output == Output.xml) {
            Asciidoctor asciidoctor = Asciidoctor.Factory.create();
            asciidoctor.convertFile(adocFile, OptionsBuilder.options().safe(SafeMode.UNSAFE).toDir(destDir).backend(backend).get());
        } else {
            //可以尝试用本地命令
            try {
                Rule<Integer, byte[]> rule = null;
                if (OS.isWindows()) {
                    rule = External.runProcess("where asciidoctorj");
                } else if (OS.isLinux()) {
                    rule = External.runProcess("which asciidoctor");
                }
                if (rule == null) return null;
                List<String> cmds = StringSplit.stringSplit(new String(rule.getValue()), "\r\n", "\n");
                if (cmds.size() > 0) {
                    for (String cmd : cmds) {
                        if ((OS.isWindows() && cmd.contains(".cmd")) || (OS.isLinux())) {
                            String command = cmd.trim() + " -b " + backend + " -D " + destDir.getAbsolutePath() + " " + adocFile.getAbsolutePath();
                            System.out.println("start adoc cmd convert " + command);
                            External.runProcess(command);
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            }

        }
        return destDir + File.separator + FileBaseName.fileBaseName(adoc) + "." + format;
    }

    /**
     * 默认html
     *
     * @param adoc
     * @param dest
     * @return
     */
    public static String convertFile(String adoc, String dest) {
        return convertFile(adoc, Output.html, dest);
    }

    /**
     * 默认相同路径
     *
     * @param adoc
     * @param output
     * @return
     */
    public static String convertFile(String adoc, Output output) {
        return convertFile(adoc, output, null);
    }

    /**
     * 默认相同路径
     *
     * @param adoc
     * @return
     */
    public static String convertFile(String adoc) {
        return convertFile(adoc, Output.html, null);
    }
}
