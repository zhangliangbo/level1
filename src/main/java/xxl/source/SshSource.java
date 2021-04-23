package xxl.source;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.swagger.annotations.ApiModel;
import io.vavr.Lazy;
import io.vavr.Tuple;
import io.vavr.Tuple5;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * @author zhangliangbo
 * @since 2021/4/23
 **/


@Slf4j
public class SshSource {

    private int localPort;

    public SshSource(int localPort) {
        this.localPort = localPort;
    }

    /**
     * [lport, rhost, rport, sesion, sshPwd]
     */
    private Lazy<Tuple5<Integer, String, Integer, Session, String>> sshInfo;

    public String connectForUri(String sshHost, Integer sshPort, String sshUser, String sshPwd, URI uri, String url) {
        if (sshHost != null && sshUser != null && sshPwd != null) {
            release();
            sshInfo = Lazy.of(() -> {
                try {
                    JSch jsch = new JSch();
                    Properties config = new Properties();
                    config.put("StrictHostKeyChecking", "no");
                    Session session = jsch.getSession(sshUser, sshHost, (sshPort == null) ? 22 : sshPort);
                    session.setPassword(sshPwd);
                    session.setConfig(config);
                    session.connect();
                    int assignPort = session.setPortForwardingL(localPort, uri.getHost(), uri.getPort());
                    return Tuple.of(assignPort, uri.getHost(), uri.getPort(), session, sshPwd);
                } catch (JSchException e) {
                    log.info("jsch error->{}", e.getMessage());
                    return null;
                }
            });
            if (sshInfo.get() != null) {
                url = url.replace(uri.getHost(), "127.0.0.1").replace(String.valueOf(uri.getPort()), String.valueOf(sshInfo.get()._1()));
            }
        } else {
            release();
            sshInfo = null;
        }
        return url;
    }

    /**
     * 如果ssh断开连接，重新连接ssh，没有则不用
     */
    public void maybeReconnectSsh() {
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
    public void release() {
        if (sshInfo != null && sshInfo.get() != null) {
            sshInfo.get()._4().disconnect();
        }
    }

}
