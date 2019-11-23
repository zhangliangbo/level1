package xxl.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 本机ip地址
 */
public class LocalHost {
    /**
     * 本地ip地址
     *
     * @return
     */
    public static String ip() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 本机名称
     *
     * @return
     */
    public static String name() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
