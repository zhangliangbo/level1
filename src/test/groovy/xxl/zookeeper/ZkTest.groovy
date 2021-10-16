package xxl.zookeeper

class ZkTest extends GroovyTestCase {

    @Override
    void setUp() throws Exception {
        Zk.use("localhost:7777")
    }

    void testCreateData() {
        def s = Zk.create("/zlb", "hello".getBytes(), 0)
        System.err.println(s)
    }

    void testSetData() {
        def s = Zk.setData("/zlb", "hello".getBytes(), 0)
        System.err.println(s)
    }

    void testGetData() {
        def s = Zk.getData("/zlb")
        System.err.println(new String(s))
    }

    void testExists() {
        def s = Zk.exists("/zlb")
        System.err.println(s)
    }

    void testDelete() {
        Zk.delete("/zlb", 1)
    }

    void testGetChildren() {
        def res = Zk.getChildren("/zlb")
        System.err.println(res)
    }

}
