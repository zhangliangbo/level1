package xxl.mathematica.jdbc;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import xxl.mathematica.io.Import;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class QuiltDemo {
    public static void main(String[] args) {
        Map<String, String> mysql = Import.importJsonAsString("D:\\zlb\\晒被机\\mysql.json");
        MysqlConnectionPoolDataSource source = new MysqlConnectionPoolDataSource();
        source.setURL(mysql.get("url"));
        source.setUser(mysql.get("username"));
        source.setPassword(mysql.get("password"));
        Option<List<Map<String, Object>>> tables = Option.of(SQLSelect.sqlSelect(source, "select table_name from information_schema.tables where table_schema='dryer'"));
        if (tables.isDefined()) {
            io.vavr.collection.List.ofAll(tables.get())
                    .map(new Function<Map<String, Object>, String>() {
                        @Override
                        public String apply(Map<String, Object> map) {
                            return (String) map.get("TABLE_NAME");
                        }
                    })
                    .filter(new Predicate<String>() {
                        @Override
                        public boolean test(String s) {
                            return s != null;
                        }
                    })
                    .forEach(new Consumer<String>() {
                        @Override
                        public void accept(String s) {
                            System.err.println(s);
                            Map<String, String> meta = SQLSelect.sqlSelectMeta(source, s);
                            if (meta == null) {
                                return;
                            }
                            io.vavr.collection.HashMap.ofAll(meta)
                                    .forEach(new Consumer<Tuple2<String, String>>() {
                                        @Override
                                        public void accept(Tuple2<String, String> tuple) {
                                            if (tuple._2.equals("DATETIME")) {
                                                System.out.println("change: " + tuple);
                                                int count = SQLSelect.sqlUpdate(source, "update " + s + " set " + tuple._1 + "='1970-01-01 00:00:00' where " + tuple._1 + "='0000-00-00 00:00:00'");
                                                System.err.println("change count: " + count);
                                            }
                                        }
                                    });
                            System.err.println(meta);
                        }
                    });
        }

    }
}
