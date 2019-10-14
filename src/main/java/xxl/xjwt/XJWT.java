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
      String xjwt = "AAABbcmcwLkBAAAAAAABjf0%3D.mGU%2BGNv242V%2BXKMEqvxbQDJiVP4a8m70MAg%2BWFvToXVIaXtS0XM3MCf75HTdOWqramgD4g%2FnHfqbHVGrtr4zC1uwRQZ7arOpsCcTMIQmylgy7FIsf1%2ByWFWqpYUxo9YOxVRuE7LCIu%2F8wO4mqdCVGkSqlYpWddlztVWHsNaX0NezamtJ5PiTgYKBQxlbEEiC7NijQuEvhEYxmyfo0kTor81HqpOm%2BTgRFqzCNWna%2B63eXRFgrssYOXS4ZXPQ3VpP7%2BlYCPMjIQazeIy%2FWJ5q5WkhZDHGf%2FgCCHy2idkzOJgr0yC%2FINczYN2fyhIbv8Ym7Zj8cuaFn%2BfkjbOh0ydMvQ%3D%3D.m0IbtGA9pCVucHDDzv%2FKGeoi2%2B9NpY4ERPK57%2FKa3W8%3D";
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
