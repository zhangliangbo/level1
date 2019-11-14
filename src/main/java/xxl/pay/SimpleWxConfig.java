package xxl.pay;

import xxl.wxpay.IWXPayDomain;
import xxl.wxpay.WXPayConfig;
import xxl.wxpay.WXPayConstants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

class SimpleWxConfig extends WXPayConfig {

  private final String appId;
  private final String certFile;
  private final String key;
  private final String mchId;

  SimpleWxConfig(String appId, String certFile, String key, String mchId) {
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
    try {
      return new FileInputStream(certFile);
    } catch (FileNotFoundException e) {
      return null;
    }
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
