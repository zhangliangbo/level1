package xxl.jdbc;


import io.vavr.Lazy;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * jdbc数据源
 * 采用dbcp2
 *
 * @author zhangliangbo
 * @since 2020/9/17
 **/
public class JdbcSource {

    private static Lazy<DataSource> dataSource;

    /**
     * 设置数据源
     *
     * @param url      地址
     * @param username 用户名
     * @param password 密码
     */
    public static void use(String url, String username, String password) {
        dataSource = Lazy.of(() -> {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(url);
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);
            return basicDataSource;
        });
    }

    /**
     * 获取数据源
     *
     * @return 数据源
     */
    public static DataSource get() {
        if (dataSource == null) {
            throw new IllegalStateException("JdbcSource.use(..)设置jdbc源");
        }
        return dataSource.get();
    }
}
