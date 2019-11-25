package xxl.mathematica.io;

import com.google.gson.reflect.TypeToken;
import xxl.mathematica.single.GsonSingle;

import java.util.Map;

/**
 * 导出字符串
 */
public class ExportString {
    /**
     * 导出json字符串
     *
     * @param object
     * @return
     */
    public static String exportStringJson(Object object) {
        return GsonSingle.instance().toJson(object);
    }

    /**
     * 导出为map字符串
     *
     * @param object
     * @return
     */
    public static Map<String, String> exportStringMap(Object object) {
        String json = GsonSingle.instance().toJson(object);
        return GsonSingle.instance().fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 导出为map对象
     *
     * @param object
     * @return
     */
    public static Map<String, Object> exportObjectMap(Object object) {
        String json = GsonSingle.instance().toJson(object);
        return GsonSingle.primitiveInstance().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
}
