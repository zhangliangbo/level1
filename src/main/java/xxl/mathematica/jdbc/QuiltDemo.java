package xxl.mathematica.jdbc;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

public class QuiltDemo {
    public static void main(String[] args) {
        MysqlConnectionPoolDataSource source = new MysqlConnectionPoolDataSource();
        source.setURL("jdbc:mysql://39.99.181.9:3306/dryer");
        source.setUser("dryer");
        source.setPassword("kKf^9!RVsG@pqNuS");
        System.err.println(SQLSelect.sqlSelect(source, "select * from qf_university"));
    }
}
