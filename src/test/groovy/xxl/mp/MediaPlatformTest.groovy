package xxl.mp

import xxl.mathematica.io.Import
import xxl.mathematica.io.ImportString

class MediaPlatformTest extends GroovyTestCase {
    String id
    String secret
    String code
    String openid
    String token
    String refresh

    @Override
    protected void setUp() throws Exception {
        def json = Import.importJson("D:\\zlb\\微信平台\\微信公众平台\\info.json")
        id = json.get("id")
        secret = json.get("secret")
        code = json.get("code")
        openid = json.get("openid")
        token = json.get("token")
        refresh = json.get("refresh")
    }

    void testWxTokenFromCode() {
        println(MediaPlatform.wxWebTokenFromCode(id, secret, code))
    }

    void testWxWebTokenValid() {
        println(MediaPlatform.wxWebTokenValid(openid, token))
    }

    void testWxWebTokenRefresh() {
        println(MediaPlatform.wxWebTokenRefresh(id, refresh))
    }

    void testWxWebUserInfo() {
        def res = MediaPlatform.wxWebUserInfo(openid, token)
        println(res)
    }

    void testAbc() {
        String json = "{\n" +
                "\t\"a\":{\n" +
                "\t\t\"b\":{\n" +
                "\t\t\t\"c\":{\n" +
                "\t\t\t\t\"d\":\"e\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}"
        def o = ImportString.importStringMapString(json)
        println(o)
    }
}
