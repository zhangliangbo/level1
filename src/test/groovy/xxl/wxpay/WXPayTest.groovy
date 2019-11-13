package xxl.wxpay

import xxl.id.ID
import xxl.mathematica.io.Import

class WXPayTest extends GroovyTestCase {
    WXPay wxPay
    boolean isSandbox = false
    String sandboxKey = "0920788a26a4db959a11015e4f2b7beb"

    @Override
    protected void setUp() throws Exception {
        Map<String, Object> hello = Import.importJson("D:\\xxlun\\xxlun\\微信平台\\微信商户平台\\hello.json")
        WXPayConfig wxPayConfig = new WXPayConfig() {
            @Override
            def String getAppID() {
                return hello.get("appid")
            }

            @Override
            def String getMchID() {
                return hello.get("mch_id")
            }

            @Override
            def String getKey() {
                if (isSandbox) {
                    return sandboxKey
                } else {
                    return hello.get("key")
                }
            }

            @Override
            def InputStream getCertStream() {
                return new FileInputStream(hello.get("cert") as String)
            }

            @Override
            def IWXPayDomain getWXPayDomain() {
                return new IWXPayDomain() {
                    @Override
                    void report(String domain, long elapsedTimeMillis, Exception ex) {
                        println("report: " + domain + ":" + elapsedTimeMillis)
                    }

                    @Override
                    IWXPayDomain.DomainInfo getDomain(WXPayConfig config) {
                        return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true)
                    }
                }
            }
        }
        wxPay = new WXPay(wxPayConfig, "http://www.weixin.qq.com/wxpay/pay.php", false, isSandbox)
    }

    void testSandboxKey() {
        println(wxPay.getSandboxSignKey())
    }

    void testOrderQuery() {
        Map<String, String> map = new HashMap<>()
        map.put("body", "小张南山店-超市")
        map.put("out_trade_no", ID.snowflake(1))
        map.put("total_fee", String.valueOf(100))
        map.put("spbill_create_ip", "121.60.117.78")
        map.put("trade_type", "NATIVE")
        println(wxPay.unifiedOrder(map))
    }
}
