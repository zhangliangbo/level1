package xxl.mp


import xxl.mathematica.io.Import

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

    void testWxWebCodeUrl() {
        println(MediaPlatform.wxWebCodeUrl(id, "123456", "https://mp.xxlun.com/quilt/authorize", true))
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

    void testServerToken() {
        def res = MediaPlatform.wxServerToken(id, secret)
        println(res)
    }

    void testSendMpMessage() {
        MpTemplateMessage msg =
                new MpTemplateMessage(openid,
                        "PVrtMMHMR3BntC7j0cUa8S42ZSsrot6OEsbLjv4dADI",
                        null,
                        "#000000")
        msg.put("first", new MsgValue("男杰你好"))
        msg.put("keyword1", new MsgValue("20170928000001"))
        msg.put("keyword2", new MsgValue("654321"))
        msg.put("keyword3", new MsgValue("标准清洗"))
        msg.put("keyword4", new MsgValue("2020.09.28 11:43"))
        msg.put("keyword5", new MsgValue("宿舍楼三楼"))
        msg.put("remark", new MsgValue("很高兴为您服务，欢迎再次使用！"))
        def res = MediaPlatform.wxSendMpMessage(token, msg)
        println(res)
    }

}
