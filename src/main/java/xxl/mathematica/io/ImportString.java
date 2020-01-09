package xxl.mathematica.io;

import com.google.gson.reflect.TypeToken;
import xxl.mathematica.single.GsonSingle;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Map;

/**
 * 导入字符串
 */
public class ImportString {
    /**
     * 导入json字符串为Map对象
     *
     * @param json
     * @return
     */
    public static Map<String, Object> importStringMapObject(String json) {
        return GsonSingle.instance().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 导入json字符串为Map字符串
     *
     * @param json
     * @return
     */
    public static Map<String, String> importStringMapString(String json) {
        return GsonSingle.oneLevelInstance().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 导入xml
     *
     * @param xml
     * @param <T>
     * @return
     */
    public static <T> T importStringXml(String xml, Class<T> cls) {
        try {
            JAXBContext context = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (Exception e) {
            return null;
        }
    }
}
