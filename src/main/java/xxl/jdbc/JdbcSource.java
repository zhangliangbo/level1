package xxl.jdbc;


import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.vavr.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;

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
    /**
     * [lport, rhost, rport, sesion, sshPwd]
     */
    private static Lazy<Tuple5<Integer, String, Integer, Session, String>> sshInfo;

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
        if (sshHost != null && sshUser != null && sshPwd != null) {
            sshInfo = Lazy.of(() -> {
                try {
                    JSch jsch = new JSch();
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking", "no");
                    Session session = jsch.getSession(sshUser, sshHost, (sshPort == null) ? 22 : sshPort);
                    session.setPassword(sshPwd);
                    session.setConfig(config);
                    session.connect();
                    int localPort = 9999;
                    int assignPort = session.setPortForwardingL(localPort, uri.getHost(), uri.getPort());
                    return Tuple.of(assignPort, uri.getHost(), uri.getPort(), session, sshPwd);
                } catch (JSchException e) {
                    log.info("jsch error->{}", e.getMessage());
                    return null;
                }
            });
            if (sshInfo.get() != null) {
                url = url.replace(uri.getHost(), "localhost").replace(String.valueOf(uri.getPort()), String.valueOf(sshInfo.get()._1()));
            }
        } else {
            if (sshInfo != null && sshInfo.get() != null) {
                sshInfo.get()._4().disconnect();
            }
            sshInfo = null;
        }
        String finalUrl = url;
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
     * 如果ssh断开连接，重新连接ssh，没有则不用
     */
    public static void maybeReconnectSsh() {
        if (sshInfo == null || sshInfo.get() == null) {
            return;
        }
        Tuple5<Integer, String, Integer, Session, String> tuple5 = sshInfo.get();
        Session session = tuple5._4();
        if (session.isConnected()) {
            log.info("jsch is connected ?->{}", session.isConnected());
            return;
        }
        try {
            JSch jsch = new JSch();
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session = jsch.getSession(session.getUserName(), session.getHost(), session.getPort());
            session.setPassword(tuple5._5());
            session.setConfig(config);
            session.connect();
            int lPort = session.setPortForwardingL(tuple5._1(), tuple5._2(), tuple5._3());
            Session finalSession = session;
            sshInfo = Lazy.of(() -> Tuple.of(lPort, tuple5._2(), tuple5._3(), finalSession, tuple5._5()));
            log.info("jsch reconnect success->{}", lPort);
        } catch (JSchException e) {
            log.info("jsch reconnect error->{}", e.getMessage());
        }
    }

    /**
     * 释放资源
     */
    public static void release() {
        if (sshInfo != null && sshInfo.get() != null) {
            sshInfo.get()._4().disconnect();
        }
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
