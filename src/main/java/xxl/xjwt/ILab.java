package xxl.xjwt;

import okhttp3.OkHttpClient;

public class ILab {
  private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

  /**
   * 获取用户信息
   *
   * @param name
   * @param pwd
   * @return
   */
  public String getUserInfo(String name, String pwd) {
    return "";
  }
}
