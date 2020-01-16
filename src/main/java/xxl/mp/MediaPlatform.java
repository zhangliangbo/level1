package xxl.mp;

import okhttp3.Request;
import okhttp3.Response;
import xxl.mathematica.io.ImportString;
import xxl.mathematica.single.OkHttpSingle;

import java.io.IOException;
import java.util.Map;

/**
 * 公众平台
 */
public class MediaPlatform {
    /**
     * 微信网页根据code获取token
     *
     * @param appId
     * @param secret
     * @param code
     * @return
     */
    public static Map<String, String> wxWebTokenFromCode(String appId, String secret, String code) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId +
                        "&secret=" + secret +
                        "&code=" + code +
                        "&grant_type=authorization_code")
                .get()
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ImportString.importStringMapString(response.body().string());
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 微信token是否有效
     *
     * @param openId
     * @param token
     * @return
     */
    public static Map<String, String> wxWebTokenValid(String openId, String token) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/auth?openid=" + openId +
                        "&access_token=" + token)
                .get()
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return ImportString.importStringMapString(response.body().string());
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
