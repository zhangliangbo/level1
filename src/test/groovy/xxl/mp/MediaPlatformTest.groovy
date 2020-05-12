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

    class Node {
        String value
        String color = "#000000"

        Node(String value) {
            this.value = value
            this.color = color
        }
    }

    class TemplateData {
        Node first = new Node("男杰你好")
        Node keyword1 = new Node("20170928000001")
        Node keyword2 = new Node("654321")
        Node keyword3 = new Node("标准清洗")
        Node keyword4 = new Node("2020.09.28 11:43")
        Node keyword5 = new Node("宿舍楼三楼")
        Node remark = new Node("很高兴为您服务，欢迎再次使用！")
    }

    void testSendMpMessage() {
        MpTemplateMessage<TemplateData> msg =
                new MpTemplateMessage<>(openid,
                        "PVrtMMHMR3BntC7j0cUa8S42ZSsrot6OEsbLjv4dADI",
                        null,
                        "#000000",
                        new TemplateData())
        def res = MediaPlatform.wxSendMpMessage(token, msg)
        println(res)
    }

}
