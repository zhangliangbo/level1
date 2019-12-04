package xxl.wxpay

import xxl.id.ID
import xxl.mathematica.image.BarcodeImage
import xxl.mathematica.image.ShowImage
import xxl.mathematica.io.Import
import xxl.pay.Pay

class WXPayTest extends GroovyTestCase {
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

    }


    void testWxOrder() {
        String outTradeNo = ID.snowflake(1)
        println(outTradeNo)
        Map<String, String> code = Pay.wxBarcode(mchId, outTradeNo, 1, "A座-1509", "这是一个商品详情iPhone", "6688", "121.60.117.78", wxNotifyPrefix + "/pay/unifiedorder")
        println(code)
        ShowImage.showImage(BarcodeImage.barcodeImage(code.get("code_url")))
    }

    void testQueryWxOrder() {
        Map<String, String> res = Pay.wxOrderQuery(mchId, "4200000425201911130947370764", null)
        println(res)
    }

    void testCancelWxOrder() {
        Map<String, String> res = Pay.wxOrderCancel(mchId, "4200000438201911131835523164")
        println(res)
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
