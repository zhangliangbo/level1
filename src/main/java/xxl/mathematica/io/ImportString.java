package xxl.mathematica.io;

import com.google.gson.reflect.TypeToken;
import xxl.mathematica.single.GsonSingle;

import java.util.Map;

/**
 * 导入字符串
 */
public class ImportString {
    /**
     * 导入json字符串为Map对象
     * @param json
     * @return
     */
    public static Map<String, Object> importStringMapObject(String json) {
        return GsonSingle.instance().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 导入json字符串为Map字符串
     * @param json
     * @return
     */
    public static Map<String, String> importStringMapString(String json) {
        return GsonSingle.primitiveInstance().fromJson(json, new TypeToken<Map<String, String>>() {}.getType());
    }
}
