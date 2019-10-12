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
      String xjwt = "AAABbcBEFcsBAAAAAAABiDA%3D.oihgY%2Bw%2FDg8JdufoizwfFs06CDajqgFF5HjiHHois8fpQmAEY2UNo6mKlUhqoOF0Rn%2BSW5%2B4jy7zOMV3ln9v4yboIF%2FaQbOdRBNW%2B95DLBpNzmCNWSENZTG8jfkyUlYKYaEJWcC1Kibj0S3hH%2BJ0C2CycasXpo6NpBkWlKBHxTPCIAXVBZBlmOrJnJdi8SW6AXQN1c5cY8Bf6mok%2B3WD0qsRtj70uHSuPGX%2Bb%2B0z5pTFc5kpvbSjhoUoY%2FElv%2BvKoDgELURGsK0yDaAySzqEK8SM%2B%2FqnY3oZHFaVr6ULZTM%3D.uN0rlc7FK%2B2JtPJ13Drym2%2BJ5Knzu3YiTZE3uO%2FfbrQ%3D";
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
