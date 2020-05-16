package xxl.mathematica.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.behavior.ICacheAccess;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 选择数据
 */
public class SQLExecute {

    private static final ICacheAccess<DataSource, QueryRunner> cache = JCS.getInstance("xxl.mathematica.jdbc");

    /**
     * 在数据源source执行sql
     *
     * @param source
     * @param sql
     * @return 列表表示集合，字典表示对象
     */
    public static List<Map<String, Object>> sqlExecute(DataSource source, String sql) {
        if (cache.get(source) == null) {
            cache.put(source, new QueryRunner(source));
        }
        try {
            return cache.get(source).query(sql, new ResultSetHandler<List<Map<String, Object>>>() {
                @Override
                public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
                    List<Map<String, Object>> list = new ArrayList<>();
                    while (rs.next()) {
                        Map<String, Object> map = new HashMap<>();
                        for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                            String key = rs.getMetaData().getColumnName(i);
                            Object value;
                            try {
                                value = rs.getObject(i);
                            } catch (Exception e) {
                                value = null;
                            }
                            map.put(key, value);
                        }
                        list.add(map);
                    }
                    return list;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
