package xxl.jdbc;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.vavr.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import xxl.source.SshSource;

import javax.sql.DataSource;
import java.net.URI;
import java.util.Arrays;
import java.util.Properties;

/**
 * jdbc数据源
 * 采用dbcp2
 *
 * @author zhangliangbo
 * @since 2020/9/17
 **/
@Slf4j
public class JdbcSource {

    private static SshSource sshSource = new SshSource(55555);

    private static Lazy<DataSource> dataSource;


    /**
     * 设置数据源
     *
     * @param url      地址
     * @param username 用户名
     * @param password 密码
     */
    public static void use(String url, String username, String password) {
        use(url, username, password, null, null, null, null);
    }

    /**
     * 使用跳板机，访问数据库
     *
     * @param url      数据库url
     * @param username 数据库用户
     * @param password 数据库密码
     * @param sshHost  跳板主机
     * @param sshPort  跳板端口
     * @param sshUser  跳板用户
     * @param sshPwd   跳板密码
     */
    public static void use(String url, String username, String password,
                           String sshHost, Integer sshPort, String sshUser, String sshPwd) {
        String jdbcPrefix = "jdbc:";
        if (!url.startsWith(jdbcPrefix)) {
            throw new IllegalArgumentException("url must start with " + jdbcPrefix);
        }
        URI uri = URI.create(url.substring(jdbcPrefix.length()));
        String finalUrl = sshSource.connectForUri(sshHost, sshPort, sshUser, sshPwd, uri, url);
        log.info("db url is {}", finalUrl);
        dataSource = Lazy.of(() -> {
            BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setUrl(finalUrl);
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

    public static void maybeReconnectSsh() {
        sshSource.maybeReconnectSsh();
    }

}
