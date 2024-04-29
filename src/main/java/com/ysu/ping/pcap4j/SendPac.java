package com.ysu.ping.pcap4j;

import com.ysu.ping.util.PcapUtil;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.ArpPacket;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.namednumber.ArpHardwareType;
import org.pcap4j.packet.namednumber.ArpOperation;
import org.pcap4j.packet.namednumber.EtherType;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

import java.net.InetAddress;

/**
 * @author Yun
 * @description: 发包线程类
 */
public class SendPac {
    PcapUtil pcapUtil = new PcapUtil();

    public void sendArp(String dstIp) throws Exception {
        InetAddress srcAddr = InetAddress.getByName(pcapUtil.getLocalIp()); //源ip地址
        PcapNetworkInterface nif = Pcaps.getDevByAddress(srcAddr); //网卡接口
        MacAddress srcMac = MacAddress.getByName(pcapUtil.getMacAddressByIp(pcapUtil.getLocalIp())); //源MAC地址
        //1.构建ARP数据包
        ArpPacket.Builder arpBuilder = new ArpPacket.Builder();
        arpBuilder
                .hardwareType(ArpHardwareType.ETHERNET) //数据链路层协议，以太网帧
                .protocolType(EtherType.IPV4) //ip协议，ipv4
                .hardwareAddrLength((byte) MacAddress.SIZE_IN_BYTES) //数据链路层协议地址长度，MAC地址长度
                .protocolAddrLength((byte) ByteArrays.INET4_ADDRESS_SIZE_IN_BYTES) //ip协议地址长度，ipv4地址长度
                .operation(ArpOperation.REQUEST) //操作，ARP请求
                .srcHardwareAddr(srcMac) //源MAC地址
                .srcProtocolAddr(srcAddr) //源ip地址
                .dstHardwareAddr(MacAddress.ETHER_BROADCAST_ADDRESS) //目的MAC地址，广播地址
                .dstProtocolAddr(InetAddress.getByName(dstIp)); //目的IP地址
        //2.构建以太网帧数据包
        EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
        etherBuilder
                .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS) //目的MAC地址，广播地址
                .srcAddr(srcMac) //源MAC地址
                .type(EtherType.ARP) //帧类型，ARP
                .payloadBuilder(arpBuilder) //服务的数据包
                .paddingAtBuild(true); //构建时填充，开启
        //3.开启发包句柄并发包
        Packet p = etherBuilder.build();
        PcapHandle sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
        sendHandle.sendPacket(p);
    }


}
