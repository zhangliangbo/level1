package xxl.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    /**
     * mac地址
     *
     * @return
     */
    public static String mac() {
        try {
            if (isWindows()) {
                return getMACAddressByWindows();
            } else {
                return getMACAddressByLinux();
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.toLowerCase().startsWith("win");
    }

    private static String getMACAddressByLinux() throws Exception {
        String[] cmd = {"ifconfig"};

        Process process = Runtime.getRuntime().exec(cmd);
        process.waitFor();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        String str1 = sb.toString();
        String str2 = str1.split("ether")[1].trim();
        String result = str2.split("txqueuelen")[0].trim();
        br.close();
        return result;
    }

    private static String getMACAddressByWindows() throws Exception {
        String result = "";
        Process process = Runtime.getRuntime().exec("ipconfig /all");
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));

        String line;
        int index;
        while ((line = br.readLine()) != null) {
            index = line.toLowerCase().indexOf("物理地址");
            if (index >= 0) {// 找到了
                index = line.indexOf(":");
                if (index >= 0) {
                    result = line.substring(index + 1).trim();
                }
                break;
            }
        }
        br.close();
        return result;
    }
}
