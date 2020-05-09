package xxl.pay

import xxl.id.ID
import xxl.mathematica.image.BarcodeImage
import xxl.mathematica.image.ShowImage
import xxl.mathematica.io.Import

class PayTest extends GroovyTestCase {
    String mchId
    String aliAppId
    String tid
    boolean isSandbox = false
    String sandboxKey = "0920788a26a4db959a11015e4f2b7beb"
    String wxNotifyPrefix

    @Override
    protected void setUp() throws Exception {
        Map<String, Object> hello = Import.importJsonAsString("D:\\zlb\\微信平台\\微信商户平台\\hello.json")
        mchId = hello.get("mch_id")
        wxNotifyPrefix = hello.get("notify")
        Pay.registerWx(hello.get("mch_id"), hello.get("cert"), hello.get("appid"), hello.get("key"))
        Map<String, Object> ali = Import.importJsonAsString("D:\\zlb\\阿里平台\\release.json")
        aliAppId = ali.get("appId")
        Pay.registerAli(ali.get("url"), aliAppId, ali.get("private"), ali.get("public"))
        Map<String, String> sb = Import.importJsonAsString("D:\\zlb\\扫呗平台\\release.json")
        Pay.registerSaobei(sb.get("url"), sb.get("mId"), sb.get("tId"), sb.get("token"))
        tid = sb.get("tId")
    }

    void testSandboxKey() {
        println(System.getProperty("java.specification.version"))
    }


    void testWxOrder() {
        String outTradeNo = ID.snowflake(1)
        println(outTradeNo)
        Map<String, String> code = Pay.wxBarcode(mchId, outTradeNo, 1, "A座-1509", "这是一个商品详情iPhone", "6688", "121.60.117.78", wxNotifyPrefix + "/pay/unifiedorder", null, "充值")
        println(code)
        ShowImage.showImage(BarcodeImage.barcodeImage(code.get("code_url")))
    }

    void testWxJsOrder() {
        String outTradeNo = ID.snowflake(1)
        println(outTradeNo)
        Map<String, String> code = Pay.wxJsOrder(mchId, outTradeNo, 1, "A座-1509", "这是一个商品详情iPhone", "6688", "121.60.117.78", wxNotifyPrefix + "/pay/unifiedorder", "*****", "充值")
        println(code)
    }

    void testQueryWxOrder() {
        Map<String, String> res = Pay.wxOrderQuery(mchId, "4200000425201911130947370764", null)
        println(res)
    }

    void testCancelWxOrder() {
        Map<String, String> res = Pay.wxOrderCancel(mchId, "4200000438201911131835523164")
        println(res)
    }

    void testWxRefundDecrypt() {
        def reqInfo = "oaRfjiQPwEPVbP2uMTXJ1fe/9VDJ5kDq0ZzuZK+owqSzlx585FqQf9M0CKALsI/Nati1c2I3lvStf4Yxx9/376q6hvQeTc7TLTEd8NFKnPkmW3+t5xpTWq29fZ+TfLvo/Fsa2r/np0Wu7L+H+RP7+O/P6UDAfhWaDQs95ICoZF41/JSUmyxGZgB3kIz4ERvP2Y5REc5cPp8VlqZpjm8qk8eo7XPb6HjGM6stVcqWWmdQSHBeEtYEfoTzu7jHNgnrEFUAoZYo974VAMLiqo3r/sgimLL5nW083w0h/P7Hx/NoZTBEb4Ycpao//hXglwcvla3QQEw77p5tb0wLNLxA2AEUWsq5gb+yUCh4I7+/3RqdFDXQqvj2wMcIzS+P77WF5WvDh4KBaJn3QiPQ2YP7h24qw8M7rfSbHHjPBPs1MfBD+FC2X3+rqeqI/7xDnvxtTqg/jZbImtpzyVs+TbR4AKqSdoZxNjKtM/mBhpgAZsikp0scaMjR5mDE5k7M6xC1nqp6vivC6Gb26huofXuZZbow9vI/0j8gJZJGtWLjrPxWSlaiHNPcpLjzVMbgEEmIzUED807C1RRS7Jsg6P3XVOaCxYol0bc08qtMVXC2AaBR8CK3epouUIzpYrXOgZXumbeXN6oFnwT9mohhAvgFkZstLZuqOCBrbpucDM+l5CLSuQaS5/+BEtZdJCN2dQZ8Q5CxPnpHuhAQyERPG6r1popamy5I8U2CBVWRjjfFmShIq9Pjh8gwx+OW4c2Oh6ht/W9jc+SyT+TT3HlBHcQCyedeMeqHGmamGHnvInuyPX1sisLpjOHkiZmv9nJmWGuYAqwlxYQ5gz61cTULkCViRPL+HVZSYaNcKMojdNsjdWTE4Luble3+ad2A1CwO4I7yf9Rs7puN1nZ93KH0T2UA6F2M0GLV1xWeG2k14VpLGS629s+mNR4QAIJ/jIDmcHbrQUiFeJYjKgX0bfRM8Wm+6DTwKwhAHvEBslPl4kkUK5j1Dn67h3Lz/8dTtCq6or/mqI8nwJJtnDZ/ShdNmMq1PU3wd08q4Ov4nkMLnDIOcEc="
        println(Pay.wxXmlToMap(Pay.wxRefundDecrypt(mchId, reqInfo)))
    }

    void testWxDownloadBill() {
        def res = Pay.wxDownloadBill(mchId, "20191113", "ALL")
        println(res)
    }

    void testAliOrder() {
        def x = Pay.aliBarcode(aliAppId, ID.snowflake(1), 1, "////////", "这是一个晚餐", "5566", "60m", null)
        println(x.get("qrCode"))
        ShowImage.showImage(BarcodeImage.barcodeImage(x.get("qrCode")))
    }

    void testQueryAliOrder() {
        def x = Pay.aliOrderQuery(aliAppId, null, "6600698769744859136")
        println(x)
    }

    void testCancelAliOrder() {
        def x = Pay.aliOrderCancel(aliAppId, null, "6600696003177746432")
        println(x)
    }

    void testRefundAliOrder() {
        def x = Pay.aliOrderRefund(aliAppId, null, "6600696003177746432", 100, "这是一个退款。。。。")
        println(x)
    }

    void testSbOrder() {
        def x = Pay.sbBarcode(tid, ID.snowflake(1), 1, "扫呗商品", "设备号", null)
        println(x)
        ShowImage.showImage(BarcodeImage.barcodeImage(x.get("qr_url")))
    }

}
