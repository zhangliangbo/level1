package xxl.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * 列出所有的数据库名称
     *
     * @return 所有的数据库
     */
    public static List<Map<String, Object>> sqlDatabases() {
        try {
            //获取连接
            Connection connection = getRunner().getDataSource().getConnection();
            //获取元数据
            DatabaseMetaData metaData = connection.getMetaData();
            //获取所有数据库列表
            ResultSet rs = metaData.getCatalogs();
            //处理数据库
            List<Map<String, Object>> mapList = new MapListHandler().handle(rs);
            rs.close();
            connection.close();
            return mapList;
        } catch (SQLException e) {
            log.info("get database error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取数据库的所有表格
     *
     * @param database 数据库
     * @return 所有表格信息
     */
    public static List<Map<String, Object>> sqlTables(String database) {
        try {
            //获取连接
            Connection connection = getRunner().getDataSource().getConnection();
            //获取元数据
            DatabaseMetaData metaData = connection.getMetaData();
            //获取表格
            ResultSet rs = metaData.getTables(database, null, null, new String[]{"TABLE"});
            //转换数据
            List<Map<String, Object>> mapList = new MapListHandler().handle(rs);
            rs.close();
            connection.close();
            return mapList;
        } catch (Exception e) {
            log.info("get table error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取所有字段信息
     *
     * @param database 数据库
     * @param table    表格名称
     * @return 字段信息
     */
    public static List<Map<String, Object>> sqlColumns(String database, String table) {
        try {
            //获取连接
            Connection connection = getRunner().getDataSource().getConnection();
            //获取元数据
            DatabaseMetaData metaData = connection.getMetaData();
            //获取表格
            ResultSet rs = metaData.getColumns(database, null, table, null);
            //转换数据
            List<Map<String, Object>> mapList = new MapListHandler().handle(rs);
            rs.close();
            connection.close();
            return mapList;
        } catch (Exception e) {
            log.info("get column error->{}", e.getMessage());
            return null;
        }
    }

    /**
     * 获取查询Runner
     *
     * @return 查询器
     */
    private static QueryRunner getRunner() {
        if (queryRunner == null || !queryRunner.getDataSource().equals(JdbcSource.get())) {
            queryRunner = new QueryRunner(JdbcSource.get());
        }
        JdbcSource.maybeReconnectSsh();
        return queryRunner;
    }
}
