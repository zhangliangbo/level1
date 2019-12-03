package xxl.saobei;

import okhttp3.*;
import xxl.mathematica.io.ExportString;
import xxl.mathematica.io.ImportString;
import xxl.mathematica.single.OkHttpSingle;

import java.io.IOException;
import java.util.Map;

public class SaobeiPay {

    private String url;
    private String mchId;
    private String terminalId;
    private String token;

    public SaobeiPay(String url, String mchId, String terminalId, String token) {
        this.url = url;
        this.mchId = mchId;
        this.terminalId = terminalId;
        this.token = token;
    }

    /**
     * 获取扫呗聚合码
     *
     * @param map
     * @return
     */
    public Map<String, String> aggregateCode(Map<String, String> map) {
        Request request = new Request.Builder()
                .url(this.url + "/pay/110/qrpay")
                .post(RequestBody.create(MediaType.get("application/json"), ExportString.exportStringJson(map)))
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
