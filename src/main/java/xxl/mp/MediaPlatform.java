package xxl.mp;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import xxl.mathematica.io.ExportString;
import xxl.mathematica.io.ImportString;
import xxl.mathematica.single.OkHttpSingle;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 公众平台
 */
public class MediaPlatform {

    /**
     * 获取token授权码的地址
     *
     * @param appId
     * @param state
     * @param redirectUri
     * @return
     */
    public static String wxWebCodeUrl(String appId, String state, String redirectUri, boolean encode) {
        String raw = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                "?appid=" + appId +
                "&response_type=code" +
                "&scope=snsapi_userinfo" +
                "&state=" + state +
                "&redirect_uri=" + URLEncoder.encode(redirectUri) +
                "#wechat_redirect";
        return encode ? URLEncoder.encode(raw) : raw;
    }

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
    public static boolean wxWebTokenValid(String openId, String token) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/auth?openid=" + openId +
                        "&access_token=" + token)
                .get()
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                Map<String, String> res = ImportString.importStringMapString(response.body().string());
                return "0".equals(res.get("errcode"));
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 刷新token
     *
     * @param appId
     * @param refreshToken
     * @return
     */
    public static Map<String, String> wxWebTokenRefresh(String appId, String refreshToken) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + appId +
                        "&refresh_token=" + refreshToken +
                        "&grant_type=refresh_token")
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
     * 拉取用户信息
     *
     * @param openId
     * @param accessToken
     * @return
     */
    public static Map<String, String> wxWebUserInfo(String openId, String accessToken) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/sns/userinfo?openid=" + openId +
                        "&access_token=" + accessToken +
                        "&lang=zh_CN")
                .get()
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                return ImportString.importStringMapString(json);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 发送微信模版消息
     *
     * @param accessToken
     * @param message
     * @param <T>
     */
    public static <T> boolean wxSendMpMessage(String accessToken, MpTemplateMessage message) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken)
                .post(RequestBody.create(MediaType.parse("application/json"), ExportString.exportStringJson(message)))
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Map<String, String> map = ImportString.importStringMapString(json);
                return map.containsKey("errcode") && "0".equals(map.get("errcode"));
            } else {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 获取后台访问的token，区别于公众号的token
     *
     * @param appId
     * @param appSecret
     * @return
     */
    public static Map<String, String> wxServerToken(String appId, String appSecret) {
        Request request = new Request.Builder()
                .url("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret)
                .get()
                .build();
        try {
            Response response = OkHttpSingle.instance().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                return ImportString.importStringMapString(json);
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
