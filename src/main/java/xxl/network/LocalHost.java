package xxl.network;

import xxl.mathematica.DeleteDuplicates;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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

    /**
     * mac地址
     *
     * @return
     */
    public static List<String> macs() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            ArrayList<String> tmpMacList = new ArrayList<>();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress addr : addressList) {
                    InetAddress ip = addr.getAddress();
                    NetworkInterface network = NetworkInterface.getByInetAddress(ip);
                    if (network == null) {
                        continue;
                    }
                    byte[] mac = network.getHardwareAddress();
                    if (mac == null) {
                        continue;
                    }
                    sb.delete(0, sb.length());
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    tmpMacList.add(sb.toString());
                }
            }
            if (tmpMacList.size() <= 0) {
                return tmpMacList;
            }
            //去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理
            return DeleteDuplicates.deleteDuplicates(tmpMacList);
        } catch (Exception e) {
            return null;
        }
    }
}
