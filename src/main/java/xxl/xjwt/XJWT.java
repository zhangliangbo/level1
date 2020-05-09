package xxl.xjwt;

import com.google.gson.Gson;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class XJWT {

    public static String dencrty(String xjwt) throws Exception {
        //获取当前时间
        long now = System.currentTimeMillis();
        //创建JWT实例
        JWT jwt = new JWT(KEY.secret, KEY.aeskey, now, KEY.issueId);
        //对数据进行url 解码
        xjwt = URLDecoder.decode(xjwt, "UTF-8");
        //解密数据
        return jwt.verifyAndDecrypt(xjwt, now);
    }

    public static String encrty(String json) throws Exception {
        //获取当前时间
        long now = System.currentTimeMillis();
        //创建JWT实例
        JWT jwt = new JWT(KEY.secret, KEY.aeskey, now, KEY.issueId);
        //创建payload
        ByteBuffer payload = ByteBuffer.allocate(1024).order(ByteOrder.BIG_ENDIAN);
        payload.put(json.getBytes(StandardCharsets.UTF_8)).flip();
        //创建out
        ByteBuffer out = ByteBuffer.allocate(1024);
        //加密数据
        jwt.encryptAndSign(JWT.Type.SYS, payload, out, now + 10 * 60 * 1000); //设置过期时间，例:10分钟
        String xjwt = new String(out.array(), out.arrayOffset(), out.remaining());
        //对数据进行url 编码
        return URLEncoder.encode(xjwt, "UTF-8");
    }

    public static void main(String[] args) {
        try {
            String data;
            // =================示例：解密xjwt (token也是一个xjwt);
            String xjwt = "AAABbcm5iSQBAAAAAAABjf0%3D.cuV2cYsU3ZgbN1fgE%2BaIiYbBFvHe3p6U7coBGqdvta%2BJZp30yqZpt7iQ9JoRlBwY0BCiVLj6m9%2BipJ%2FRny%2FhaVzwMhfTvYzYCgV6Vi40%2BEDHNquID9KKNeHx7Hlw24NY%2BJavjvRxbkuUt8t6mutAFYDCm%2BU33gdObfuwn%2Fr2smwA7HyFNMTjo%2BpNdp5SONHC%2BuDLsYiv7Ad0stBE2LudEjd9yVydEc9SpJYa9Ma2TFLIbAkpSPOnZaNJkkAm5RFcuGe9TRdiBtin6CfYgFdpVpiAQt8tAotcNvowld%2BilPIh%2F04sWm9X4VOS1qiiGmTPobJRZOB0hit5TTdGXkR%2Bzg%3D%3D.xBAD%2F8s%2Bmn9OQlKKvsBVxy9SdQUJ%2F%2FzZrWHOqM7bd7I%3D";
            data = dencrty(xjwt);
            System.out.println(data);

            // =================示例：生成xjwt
            Map<String, String> param = new HashMap<>();
            param.put("username", "test");
            param.put("issuerId", KEY.issueId.toString());
            String json = new Gson().toJson(param);

            data = encrty(json);
            System.out.println(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
