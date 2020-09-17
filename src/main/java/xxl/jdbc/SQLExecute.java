package xxl.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据操作
 *
 * @author zhang
 */
@Slf4j
public class SQLExecute {

    private static QueryRunner queryRunner = null;

    /**
     * 结果转成【字典集合】
     */
//    private static class MapListHandler implements ResultSetHandler<List<Map<String, Object>>> {
//        @Override
//        public List<Map<String, Object>> handle(ResultSet rs) throws SQLException {
//            List<Map<String, Object>> list = new ArrayList<>();
//            while (rs.next()) {
//                Map<String, Object> map = new HashMap<>(1);
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    String name = rs.getMetaData().getColumnName(i);
//                    Object value;
//                    try {
//                        value = rs.getObject(i);
//                    } catch (Exception e) {
//                        value = null;
//                    }
//                    map.put(name, value);
//                }
//                list.add(map);
//            }
//            return list;
//        }
//    }

    /**
     * 查询
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 列表表示集合，字典表示对象
     */
    public static List<Map<String, Object>> sqlSelect(String sql, Object... params) {
        try {
            return getRunner().query(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            log.info("select error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 元数据
     *
     * @param tableName 表名
     * @return 列表表示集合，字典表示对象
     */
    public static Map<String, String> sqlSelectMeta(String tableName) {
        try {
            return getRunner().query("select * from " + tableName + " limit 1", rs -> {
                Map<String, String> map = new HashMap<>(1);
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    String name = rs.getMetaData().getColumnName(i);
                    String type = rs.getMetaData().getColumnTypeName(i);
                    map.put(name, type);
                }
                return map;
            });
        } catch (SQLException e) {
            log.info("meta error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 增删改
     *
     * @param sql    sql语句
     * @param params 参数
     * @return 列表表示集合，字典表示对象
     */
    public static int sqlUpdate(String sql, Object... params) {
        try {
            return getRunner().update(sql, params);
        } catch (SQLException e) {
            log.info("update error->{}", e.getMessage());
            return -1;
        }
    }

    /**
     * 批量插入
     *
     * @param sql    sql语句
     * @param params 参数
     * @return
     */
    public static List<Map<String, Object>> sqlInsertBatch(String sql, Object[][] params) {
        try {
            return getRunner().insertBatch(sql, new MapListHandler(), params);
        } catch (SQLException e) {
            log.info("insert batch error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取查询Runner
     *
     * @return 查询器
     */
    private static QueryRunner getRunner() {
        if (queryRunner == null) {
            queryRunner = new QueryRunner(JdbcSource.get());
        }
        return queryRunner;
    }
}
