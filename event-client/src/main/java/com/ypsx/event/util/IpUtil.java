package com.ypsx.event.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 功能：获取本机ip
 *
 * @author chuchengyi
 */
public class IpUtil {


    public static final String UN_KNOWN_HOST = "unknown ip";

    /**
     * 功能：获取到本机的ip地址
     *
     * @return
     */
    public static InetAddress getLocalHostDress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            Enumeration ifFaceList = NetworkInterface.getNetworkInterfaces();
            while (ifFaceList.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) ifFaceList.nextElement();
                // 在所有的接口下再遍历IP
                Enumeration addressList = networkInterface.getInetAddresses();
                while (addressList.hasMoreElements()) {
                    InetAddress address = (InetAddress) addressList.nextElement();
                    // 排除loopback类型地址
                    if (!address.isLoopbackAddress()) {
                        if (address.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return address;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = address;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            return jdkSuppliedAddress;
        } catch (Exception e) {

        }
        return null;
    }


    public static void main(String[] args) {
        InetAddress address = getLocalHostDress();
        System.out.println(address.getHostAddress());
    }
}
