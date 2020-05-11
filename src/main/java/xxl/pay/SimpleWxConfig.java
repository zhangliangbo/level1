package xxl.pay;

import xxl.wx.pay.IWXPayDomain;
import xxl.wx.pay.WXPayConfig;
import xxl.wx.pay.WXPayConstants;

import java.io.InputStream;

class SimpleWxConfig extends WXPayConfig {

    private final String appId;
    private final InputStream certFile;
    private final String key;
    private final String mchId;

    SimpleWxConfig(String appId, InputStream certFile, String key, String mchId) {
        this.appId = appId;
        this.certFile = certFile;
        this.key = key;
        this.mchId = mchId;
    }


    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return certFile;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
