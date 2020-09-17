package xxl.jdbc

class SQLExecuteTest extends GroovyTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp()
        JdbcSource.use("jdbc:mysql://localhost:3306/quilt", "xxl", "civic")
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown()

    }

    void testSqlSelect() {
        println(SQLExecute.sqlSelect("select * from quilt_hello"))
    }

    void testSqlInsert() {
        println(SQLExecute.sqlUpdate("insert into quilt_hello (id,name,number,version) values (?,'zlb',1,1)", 7))
    }

    void testSqlInsertBatch() {
        println(
                SQLExecute.sqlInsertBatch("insert into quilt_hello (id,name,number,version) values (?,?,?,?)",
                        [[8, "8", 8, 8], [9, "9", 9, 9]] as Object[][]
                )
        )
    }

    void testMeta() {
        println(SQLExecute.sqlSelectMeta("quilt_hello"))
    }

}
