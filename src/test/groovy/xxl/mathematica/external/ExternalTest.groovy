package xxl.mathematica.external

class ExternalTest extends GroovyTestCase {
    void testExec() {
        println(new String(External.runProcess(new File("C:\\Users\\zhang\\Desktop\\file_server_main_jar"),'mathematica.exe'), 'GBK'))
    }

    void testRun() {
        println(External.run('git --help'))
    }
}
