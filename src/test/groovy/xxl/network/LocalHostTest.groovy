package xxl.network

class LocalHostTest extends GroovyTestCase {
    void testLocalIp() {
        println(LocalHost.ip())
        println(LocalHost.name())
        println(LocalHost.macs())
    }
}
