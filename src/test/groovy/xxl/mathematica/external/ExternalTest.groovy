package xxl.mathematica.external

class ExternalTest extends GroovyTestCase {
    void testExec() {
        println(new String(External.runProcess(new File("C:\\Users\\zhang\\Desktop\\file_server_main_jar"), 'mathematica.exe'), 'GBK'))
    }

    void testRun() {
        println(External.run('git --help'))
    }

    void testSendMail() {
        println(External.sendMail("forbidden@aliyun.com", "附件", "<img src=\"https://himg2.huanqiucdn.cn/attachment2010/2019/1101/20191101072831653.jpg\"/>", [new File("C:\\Users\\zhang\\Desktop\\shu.jpg")], "2472110501@qq.com", "smtp.aliyun.com", "forbidden@aliyun.com", "forbidden"))
    }
}
