package xxl.location;

import com.google.gson.JsonObject;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import xxl.codec.digest.DigestUtils;
import xxl.mathematica.single.GsonSingle;
import xxl.mathematica.single.OkHttpSingle;

import java.io.IOException;

/**
 * 位置服务
 */
public class Location {
    /**
     * 微信位置服务
     *
     * @param lon
     * @param lat
     * @param key
     * @param sk
     * @return
     */
    public static String wxLocation(double lon, double lat, String key, String sk) {
        String raw = "/ws/geocoder/v1?key=" + key + "&location=" + lat + "," + lon;
        String sign = DigestUtils.md5Hex((raw + sk).getBytes());
        String path = raw + "&sig=" + sign;
        String url = "https://apis.map.qq.com" + path;
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String json = responseBody.string();
                JsonObject root = GsonSingle.instance().fromJson(json, JsonObject.class);
                if (root.has("status") && root.get("status").getAsInt() == 0 && root.has("result") && root.get("result").getAsJsonObject().has("address")) {
                    return root.get("result").getAsJsonObject().get("address").getAsString();
                }
                return null;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

}
