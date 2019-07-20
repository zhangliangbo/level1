package xxl.mathematica.process

class ShellTest extends GroovyTestCase {
    void testExec() {
        println(new String(Shell.exec(new File("C:\\Users\\zhang\\Desktop\\file_server_main_jar"),'mathematica.exe'), 'GBK'))
    }
}
