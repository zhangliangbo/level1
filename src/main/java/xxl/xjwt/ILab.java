package xxl.xjwt;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import xxl.mathematica.Hash;
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

    public String getToken(String xjwt) {
        try {
            return XJWT.dencrty(xjwt);
        } catch (Exception e) {
            return null;
        }
    }

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
        String pwdHex = Hash.encodeHexString(Hash.hashString(pwd, Hash.Algorithm.SHA256));
        String password = Hash.encodeHexString(Hash.hashString(nonce + pwdHex.toUpperCase() + cnonce, Hash.Algorithm.SHA256)).toUpperCase();
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

    /**
     * 解密获取用户名称，如果成功则通过实验
     *
     * @param token
     * @return
     */
    public String getUserNameFromToken(String token) {
        String tokenDe = getToken(token);
        if (tokenDe == null) {
            return null;
        }
        Map<String, String> tokenMap = gson.fromJson(tokenDe, new TypeToken<Map<String, String>>() {
        }.getType());
        return tokenMap.get("un");
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

    public String uploadResult(String username,
                               String model, int status, int score,
                               long startDate, int timeUsed, File file) {
        Map<String, Object> query = new HashMap<>();
        query.put("username", username);
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

    public String uploadResult(String username, String pwd,
                               String model, int status, int score,
                               long startDate, int timeUsed, File file) {
        String[] user = getUserName(username, pwd);
        if (user == null) {
            return null;
        }
        return uploadResult(user[0], model, status, score, startDate, timeUsed, file);
    }

    /**
     * 从Token获取用户信息并上报分数
     *
     * @param token
     * @param model
     * @param status
     * @param score
     * @param startDate
     * @param timeUsed
     * @param file
     * @return
     */
    public String uploadResultFromToken(String token,
                                        String model, int status, int score,
                                        long startDate, int timeUsed, File file) {

        String username = getUserNameFromToken(token);
        if (username == null) {
            return null;
        }
        return uploadResult(username, model, status, score, startDate, timeUsed, file);
    }

    public String uploadState(String username) {
        Map<String, String> map = new HashMap<>();
        map.put("issuerId", String.valueOf(KEY.issueId));
        map.put("username", username);
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

    public String uploadState(String username, String pwd) {
        String[] user = getUserName(username, pwd);
        if (user == null) {
            return null;
        }
        return uploadState(user[0]);
    }

    /**
     * 从token获取用户信息并上报状态
     *
     * @param token
     * @return
     */
    public String uploadStateFromToken(String token) {
        String username = getUserNameFromToken(token);
        if (username == null) {
            return null;
        }
        return uploadState(username);
    }

    /**
     * 获取上报的文件ID
     *
     * @param file
     * @return
     */
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
