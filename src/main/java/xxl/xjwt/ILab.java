package xxl.xjwt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import xxl.codec.digest.DigestUtils;
import xxl.mathematica.RandomChoice;
import xxl.mathematica.single.GsonSingle;
import xxl.mathematica.single.OkHttpSingle;
import xxl.mathematica.string.StringJoin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ILab {
  //  private String baseUrl = "http://202.205.145.156:8017";
  private String baseUrl = "http://www.ilab-x.com";
  private String[] chars = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
  private Gson gson = GsonSingle.instance();
  private OkHttpClient okHttpClient = OkHttpSingle.instance();

  /**
   * 获取用户信息
   *
   * @param name
   * @param pwd
   * @return
   */
  public String getUserInfo(String name, String pwd) {
    String nonce = StringJoin.stringJoin(RandomChoice.randomChoice(Arrays.asList(chars), 16));
    String cnonce = StringJoin.stringJoin(RandomChoice.randomChoice(Arrays.asList(chars), 16));
    String password = DigestUtils.sha256Hex(nonce + DigestUtils.sha256Hex(pwd).toUpperCase() + cnonce).toUpperCase();
    Request request = new Request.Builder()
        .url(baseUrl + "/sys/api/user/validate?username=" + name + "&password=" + password + "&nonce=" + nonce + "&cnonce=" + cnonce)
        .get()
        .build();
    try {
      Response response = okHttpClient.newCall(request).execute();
      return response.body().string();
    } catch (IOException e) {
      return null;
    }
  }

  public String uploadFile(File file) {
    String xjwt = null;
    try {
      xjwt = XJWT.encrty("SYS");
    } catch (Exception e) {
      return null;
    }
    int totalChunks = 1;
    int current = 0;
    String fileName = file.getName();
    long chunkSize = 1048576;
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
    Request request = new Request.Builder()
        .url(baseUrl + "/project/log/attachment/upload?xjwt=" + xjwt + "&totalChunks=" + totalChunks + "&current=" + current + "&filename=" + fileName + "&chunkSize=" + chunkSize)
        .post(requestBody)
        .build();
    try {
      Response response = okHttpClient.newCall(request).execute();
      return response.body().string();
    } catch (IOException e) {
      return null;
    }
  }

  public String uploadResult(String username, String pwd,
                             String model, int status, int score,
                             long startDate, int timeUsed, File file) {
    Map<String, Object> query = new HashMap<>();
    String[] user = getUserName(username, pwd);
    if (user == null) {
      return null;
    }
    query.put("username", user[0]);
    query.put("projectTitle", "人工社会建模虚拟仿真实验项目");
    query.put("childProjectTitle", model);
    query.put("status", status);
    query.put("score", score);
    query.put("startDate", startDate);
    query.put("endDate", startDate + timeUsed * 60000);
    query.put("timeUsed", timeUsed);
    query.put("issuerId", String.valueOf(KEY.issueId));
    if (file != null) {
      String fileId = getFileId(file);
      query.put("attachmentId", fileId);
    }
    String xjwt;
    try {
      xjwt = XJWT.encrty(gson.toJson(query));
    } catch (Exception e) {
      return null;
    }
    Request request = new Request.Builder()
        .url(baseUrl + "/project/log/upload?xjwt=" + xjwt)
        .build();
    try {
      Response response = okHttpClient.newCall(request).execute();
      return response.body().string();
    } catch (IOException e) {
      return null;
    }
  }

  public String uploadState(String username, String pwd) {
    Map<String, String> map = new HashMap<>();
    map.put("issuerId", String.valueOf(KEY.issueId));
    String[] user = getUserName(username, pwd);
    if (user == null) {
      return null;
    }
    map.put("username", user[0]);
//    map.put("name", user[1]);
    String json = gson.toJson(map);
    String xjwt;
    try {
      xjwt = XJWT.encrty(json);
    } catch (Exception e) {
      return null;
    }
    Request request = new Request.Builder()
        .url(baseUrl + "/third/api/test/result/upload?xjwt=" + xjwt)
        .get()
        .build();
    try {
      Response response = okHttpClient.newCall(request).execute();
      return response.body().string();
    } catch (IOException e) {
      return null;
    }
  }

  private String getFileId(File file) {
    String json = uploadFile(file);
    if (json != null) {
      Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
      }.getType());
      return map.get("id");
    } else {
      return null;
    }
  }

  public String getToken(String xjwt) {
    try {
      return XJWT.dencrty(xjwt);
    } catch (Exception e) {
      return null;
    }
  }

  private String[] getUserName(String name, String pwd) {
    String userJson = getUserInfo(name, pwd);
    if (userJson == null) {
      return null;
    }
    Map<String, String> mapUser = gson.fromJson(userJson, new TypeToken<Map<String, String>>() {
    }.getType());
    return new String[]{mapUser.get("username"), mapUser.get("name")};
  }
}
