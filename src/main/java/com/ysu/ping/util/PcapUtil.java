package com.ysu.ping.util;

import org.pcap4j.core.PcapAddress;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;

import java.net.InetAddress;

/**
 * @author Yun
 * @description: TODO
 */
public class PcapUtil {

    /**
     * @description: 获取本机ip
     * @param:
     * @return: 字符串
     */
    public String getLocalIp() throws Exception {
        InetAddress addr = null;
        PcapNetworkInterface[] devices = Pcaps.findAllDevs().toArray(new PcapNetworkInterface[0]);
        for (PcapNetworkInterface nif : devices
        ) {
            for (PcapAddress pca : nif.getAddresses()
            ) {
                //特殊情况，排除VM box的虚拟网卡
                if (!nif.getDescription().equals("Oracle")) {
                    InetAddress address = pca.getAddress();
                    //排除回环地址
                    if (address.isSiteLocalAddress() && !address.isLoopbackAddress()) {
                        addr = address;
                    }
                }

            }
        }

        if (addr != null) {
            return addr.toString().substring(1);
        } else return "未找到ip";
    }

    /**
     * @description: 通过ip地址获取mac地址
     * @param: ip地址
     * @return: 字符串
     */
    public String getMacAddressByIp(String ip) throws Exception {
        PcapNetworkInterface nif = Pcaps.getDevByAddress(InetAddress.getByName(ip));
        return nif.getLinkLayerAddresses().get(0).toString();
    }

}
