package xxl.network;


import xxl.mathematica.DeleteDuplicates;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * 本机ip地址
 */
public class LocalHost {
    /**
     * 主机名称
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
     * name ip mac地址
     *
     * @return
     */
    public static List<String[]> nameIpMac(boolean onlyIpv4) {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            ArrayList<String[]> tempList = new ArrayList<>();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                List<InterfaceAddress> addressList = networkInterface.getInterfaceAddresses();
                for (InterfaceAddress addr : addressList) {
                    String[] information = new String[3];
                    InetAddress inetAddress = addr.getAddress();
                    information[0] = inetAddress.getHostName();
                    information[1] = inetAddress.getHostAddress();
                    boolean ipv6Q = information[1].contains("%");
                    if (ipv6Q && onlyIpv4) {
                        continue;
                    }
                    NetworkInterface network = NetworkInterface.getByInetAddress(inetAddress);
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
                    information[2] = sb.toString();
                    tempList.add(information);
                }
            }
            if (tempList.size() <= 0) {
                return tempList;
            }
            //去重，别忘了同一个网卡的ipv4,ipv6得到的mac都是一样的，肯定有重复，下面这段代码是。。流式处理
            return DeleteDuplicates.deleteDuplicates(tempList, new BiPredicate<String[], String[]>() {
                @Override
                public boolean test(String[] strings, String[] strings2) {
                    if (strings.length != strings2.length) return false;
                    for (int i = 0; i < strings.length; i++) {
                        if (!strings[i].equals(strings2[i])) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }
}
