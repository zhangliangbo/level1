package xxl.mathematica.jdbc;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import xxl.mathematica.io.Import;

import java.util.Map;

public class QuiltDemo {
    public static void main(String[] args) {
        Map<String, String> mysql = Import.importJsonAsString("D:\\zlb\\晒被机\\mysql.json");
        MysqlConnectionPoolDataSource source = new MysqlConnectionPoolDataSource();
        source.setURL(mysql.get("url"));
        source.setUser(mysql.get("username"));
        source.setPassword(mysql.get("password"));
        System.err.println(SQLExecute.sqlExecute(source, "select * from qf_building"));
    }
}
