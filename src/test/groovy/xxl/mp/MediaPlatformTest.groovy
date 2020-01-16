package xxl.mp

import xxl.mathematica.io.Import

class MediaPlatformTest extends GroovyTestCase {
    String id
    String secret
    String code

    @Override
    protected void setUp() throws Exception {
        def json = Import.importJson("D:\\zlb\\微信平台\\微信公众平台\\info.json")
        id = json.get("id")
        secret = json.get("secret")
        code = json.get("code")
    }

    void testWxTokenFromCode() {
        println(MediaPlatform.wxTokenFromCode(id, secret, code))
    }
}
