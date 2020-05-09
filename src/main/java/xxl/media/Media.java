package xxl.media;

import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import xxl.mathematica.predication.MemberQ;

import java.util.List;

public class Media {
    public static void main(String[] args) throws MimeTypeException {
        String type = "image/jpeg";
        System.err.println(extension(type));
        System.err.println(extensions(type));
        System.err.println(in("jpg", type));
    }

    /**
     * 获取媒体类型的后缀
     *
     * @param name
     * @return
     */
    public static String extension(String name) {
        MimeTypes all = MimeTypes.getDefaultMimeTypes();
        try {
            MimeType type = all.forName(name);
            System.err.println(type.getType().getBaseType());
            return type.getExtension();
        } catch (MimeTypeException e) {
            return null;
        }
    }

    /**
     * 获取媒体类型的所有后缀
     *
     * @param name
     * @return
     */
    public static List<String> extensions(String name) {
        MimeTypes all = MimeTypes.getDefaultMimeTypes();
        try {
            MimeType type = all.forName(name);
            return type.getExtensions();
        } catch (MimeTypeException e) {
            return null;
        }
    }

    /**
     * 后缀是否属于类型的后缀
     *
     * @param extension
     * @param name
     * @return
     */
    public static boolean in(String extension, String name) {
        List<String> extensions = extensions(name);
        if (extensions != null) {
            return MemberQ.memberQ(extensions, "." + extension);
        } else {
            return false;
        }
    }
}
