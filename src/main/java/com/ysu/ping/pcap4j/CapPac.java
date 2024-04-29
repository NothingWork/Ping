package com.ysu.ping.pcap4j;

import com.ysu.ping.util.PcapUtil;
import org.pcap4j.core.BpfProgram;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;

import java.net.InetAddress;

/**
 * @author Yun
 * @description: 抓包工具包
 */
public class CapPac extends Thread{
    PcapUtil pcapUtil = new PcapUtil();

    @Override
    public void run() {
        super.run();
        try {
            capPacket(pcapUtil.getLocalIp());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @description: 抓包
     * @param: ip地址
     * @return: void
     */
    public void capPacket(String ip) throws Exception {
        //1.获取网络接口
        InetAddress addr = InetAddress.getByName(ip);
        PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);
        //2.开启pcap句柄(长度，混杂模式，超时时间)
        PcapHandle handle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        //3.设置过滤器
        handle.setFilter("arp", BpfProgram.BpfCompileMode.OPTIMIZE);
        while (true) {
            //4.抓包
            Packet packet = handle.getNextPacket();
//            handle.close();
            //5.获取抓包信息
            if (packet != null) {
                System.out.println(packet);
            }
        }
    }
}
