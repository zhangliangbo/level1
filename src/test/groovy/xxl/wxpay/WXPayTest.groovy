package xxl.wxpay

import xxl.id.ID
import xxl.mathematica.image.BarcodeImage
import xxl.mathematica.image.ShowImage
import xxl.mathematica.io.Import
import xxl.pay.Pay

class WXPayTest extends GroovyTestCase {
    String mchId
    String aliAppId
    boolean isSandbox = false
    String sandboxKey = "0920788a26a4db959a11015e4f2b7beb"

    @Override
    protected void setUp() throws Exception {
        Map<String, Object> hello = Import.importJson("D:\\xxlun\\xxlun\\微信平台\\微信商户平台\\hello.json")
        mchId = hello.get("mch_id")
        Pay.registerWx(hello.get("mch_id") as String, hello.get("cert") as String, hello.get("appid") as String, hello.get("key") as String)
        Map<String, Object> ali = Import.importJson("D:\\xxlun\\xxlun\\阿里平台\\ali.json")
        aliAppId = ali.get("appId") as String
        Pay.registerAli(ali.get("url") as String, aliAppId, ali.get("private") as String, ali.get("public") as String)
    }

    void testSandboxKey() {

    }


    void testWxOrder() {
        Map<String, String> code = Pay.wxBarcode(mchId, ID.snowflake(1), 1, "A座-1509", "这是一个商品详情iPhone", "6688", "121.60.117.78", null);
        println(code)
        ShowImage.showImage(BarcodeImage.barcodeImage(code.get("code_url")))
    }

    void testQueryWxOrder() {
        Map<String, String> res = Pay.wxOrderQuery(mchId, "4200000438201911131835523164", null)
        println(res)
    }

    void testCancelWxOrder() {
        Map<String, String> res = Pay.wxOrderCancel(mchId, "4200000438201911131835523164", null)
        println(res)
    }

    void testAliOrder() {
        def x = Pay.aliBarcode(aliAppId, ID.snowflake(1), 500, "////////", "这是一个晚餐", "5566", "60m", null)
        println(x.outTradeNo)
        ShowImage.showImage(BarcodeImage.barcodeImage(x.qrCode))
    }

    void testQueryAliOrder() {
        def x = Pay.aliOrderQuery(aliAppId, null, "6600698769744859136", null)
        println(x)
    }

    void testCancelAliOrder() {
        def x = Pay.aliOrderCancel(aliAppId, null, "6600696003177746432", null)
        println(x)
    }

    void testRefundAliOrder() {
        def x = Pay.aliOrderRefund(aliAppId, null, "6600696003177746432", 100, "这是一个退款。。。。", null)
        println(x)
    }

}
