package xxl.swagger;

import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * swagger文档
 */
public class SwaggerDoc {
    /**
     * 格式
     */
    public enum Format {
        ascii, markdown, text
    }

    /**
     * swagger文档
     *
     * @param url
     * @param format
     * @param dest
     * @return
     */
    public static String swaggerDoc(String url, Format format, String dest, boolean isDir) {
        MarkupLanguage markupLanguage;
        switch (format) {
            case ascii:
                markupLanguage = MarkupLanguage.ASCIIDOC;
                break;
            case markdown:
                markupLanguage = MarkupLanguage.MARKDOWN;
                break;
            default:
                markupLanguage = MarkupLanguage.CONFLUENCE_MARKUP;
                break;
        }
        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(markupLanguage)
                .withOutputLanguage(Language.ZH)
                .withPathsGroupedBy(GroupBy.TAGS)
                .withGeneratedExamples()
                .withoutInlineSchema()
                .build();
        try {
            File file = new File(dest);
            if (file.exists()) {
                //文件存在，则直接根据文件来判断是否是目录
                isDir = file.isDirectory();
            }
            if (isDir) {
                Swagger2MarkupConverter.from(new URL(url))
                        .withConfig(config)
                        .build()
                        .toFolder(Paths.get(file.getAbsolutePath()));
                return file.getAbsolutePath();
            } else {
                Swagger2MarkupConverter.from(new URL(url))
                        .withConfig(config)
                        .build()
                        .toFile(Paths.get(file.getAbsolutePath()));
                String suffix;
                switch (format) {
                    case ascii:
                        suffix = ".adoc";
                        break;
                    case markdown:
                        suffix = ".md";
                        break;
                    default:
                        suffix = ".txt";
                        break;
                }
                file.delete();//如果有旧的文件，则删除
                return file.getAbsolutePath() + suffix;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * 默认导出到临时文件
     *
     * @param url
     * @param format
     * @return
     */
    public static String swaggerDoc(String url, Format format) {
        try {
            File temp = File.createTempFile("swagger", "");
            return swaggerDoc(url, format, temp.getAbsolutePath(), false);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 默认ascii格式
     *
     * @param url
     * @param dest
     * @return
     */
    public static String swaggerDoc(String url, String dest, boolean isDir) {
        return swaggerDoc(url, Format.ascii, dest, isDir);
    }

    /**
     * 默认ascii格式
     *
     * @param url
     * @return
     */
    public static String swaggerDoc(String url) {
        return swaggerDoc(url, Format.ascii);
    }

}
