package xxl.jdbc

class SQLExecuteTest extends GroovyTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp()
        JdbcSource.use("jdbc:mysql://127.0.0.1:3306/test", "root", "civic")
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown()

    }

    void testSqlSelect() {
        println(SQLExecute.sqlSelect("select * from quilt_hello"))
    }

    void testSqlIn() {
        List<String> res = new ArrayList<>()
        for (int i = 0; i < 10000; i++) {
            res.add(i as String)
        }
        String inStr = String.join(",", res)
        println(SQLExecute.sqlSelect("select * from test_in where num in (" + inStr + ")").size())
    }

    void testSqlInsert() {
        println(SQLExecute.sqlUpdate("insert into `test_in` (`num`) values (?)", 7))
    }

    void testSqlInsertBatch() {
        Object[][] objects = new Object[10000][]

        for (int i = 0; i < 10000; i++) {
            objects[i] = new Object[]{i}
        }
        println(
                SQLExecute.sqlBatch("insert into `test_in` (`num`) values (?)", objects)
        )
    }

    void testDatabase() {
        println(SQLExecute.sqlDatabases())
    }

    void testTable() {
        println(SQLExecute.sqlTables("test"))
    }

    void testColumn() {
        println(SQLExecute.sqlColumns("test", "test_in"))
    }

    void testSSH() {

    }

}
