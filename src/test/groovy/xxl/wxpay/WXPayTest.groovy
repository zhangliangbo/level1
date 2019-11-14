package xxl.wxpay

import xxl.id.ID
import xxl.mathematica.image.BarcodeImage
import xxl.mathematica.image.ShowImage
import xxl.mathematica.io.Import
import xxl.pay.Pay

class WXPayTest extends GroovyTestCase {
    String mchId
    boolean isSandbox = false
    String sandboxKey = "0920788a26a4db959a11015e4f2b7beb"

    @Override
    protected void setUp() throws Exception {
        Map<String, Object> hello = Import.importJson("D:\\xxlun\\xxlun\\微信平台\\微信商户平台\\hello.json")
        mchId = hello.get("mch_id")
        Pay.registerWx(hello.get("mch_id") as String, hello.get("cert") as String, hello.get("appid") as String, hello.get("key") as String, "http://www.weixin.qq.com/wxpay/pay.php")
    }

    void testSandboxKey() {
        println(wxPay.getSandboxSignKey())
    }

    void testUnifyQuery() {
        Map<String, String> code = Pay.wxBarcode(mchId, "121.60.117.78", ID.snowflake(1), "A座-1509", 1)
        println(code)
        ShowImage.showImage(BarcodeImage.barcodeImage(code.get("code_url")))
    }

    void testQueryByWx() {
        Map<String, String> res = Pay.wxOrderQueryByWxNum(mchId, "4200000438201911131835523164")
        println(res)
    }

    void testQueryByMch() {
        println(Pay.wxOrderQueryByMchNum(mchId, "6600286964014911488"))
    }

}
