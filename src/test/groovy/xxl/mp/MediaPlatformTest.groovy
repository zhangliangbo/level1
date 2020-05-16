package xxl.mp


import xxl.mathematica.io.ExportString
import xxl.mathematica.io.Import
import xxl.mp.aes.WXBizMsgCrypt
import xxl.wx.pay.WXPayUtil

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
        msg.put("first", new TemplateMessageValue("**你好"))
        msg.put("keyword1", new TemplateMessageValue("20170928000001"))
        msg.put("keyword2", new TemplateMessageValue("654321"))
        msg.put("keyword3", new TemplateMessageValue("标准清洗"))
        msg.put("keyword4", new TemplateMessageValue("2020.09.28 11:43"))
        msg.put("keyword5", new TemplateMessageValue("宿舍楼三楼"))
        msg.put("remark", new TemplateMessageValue("很高兴为您服务，欢迎再次使用！"))
        def res = MediaPlatform.wxSendMpMessage(token, msg)
        println(res)
    }

    void testSendMpMessage2() {
        MpTemplateMessage msg =
                new MpTemplateMessage(openid,
                        "RvVNCvhhW74PSt10dWQz_w_TRmqyLcU4OWWosu26E88",
                        null,
                        "#000000")
        msg.put("first", new TemplateMessageValue("**你好"))
        msg.put("keyword1", new TemplateMessageValue("20170928000001"))
        msg.put("keyword2", new TemplateMessageValue("654321"))
        msg.put("keyword3", new TemplateMessageValue("标准清洗"))
        msg.put("keyword4", new TemplateMessageValue("2020.09.28 11:43"))
        msg.put("keyword5", new TemplateMessageValue("宿舍楼三楼"))
        msg.put("remark", new TemplateMessageValue("您的服务即将开始，请尽快去晒被！"))
        def res = MediaPlatform.wxSendMpMessage(token, msg)
        println(res)
    }

    void testWxMessageDecrypt() {
        //
        // 第三方回复公众平台
        //
        EventMessage eventMessage = new EventMessage(
                "toUser",
                "fromUser",
                System.currentTimeMillis(),
                "event",
                "subscribe")
        // 需要加密的明文
        String encodingAesKey = "DwtVpXo1JCuoPz9OoPKJeHrdmLwiSzBuXjil15nGwr8";
        String token = "XXL365421xxl";
        String timestamp = "1409304348";
        String nonce = "xxxxxx";
        String appId = "wxff15abc0c1xyz90f";
        String replyMsg = WXPayUtil.mapToXml(ExportString.exportStringMap(eventMessage))

        println("明文:" + replyMsg)

        WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
        String miwen = pc.encryptMsg(replyMsg, timestamp, nonce);
        System.out.println("加密后: " + miwen);

        Map<String, String> map = WXPayUtil.xmlToMap(miwen);

        //
        // 公众平台发送消息给第三方，第三方处理
        //

        // 第三方收到公众号平台发送的消息
        String result2 = pc.decryptMsg(map.get("MsgSignature"), timestamp, nonce, miwen);
        System.out.println("解密后明文: " + result2);

        //pc.verifyUrl(null, null, null, null);
    }

}
