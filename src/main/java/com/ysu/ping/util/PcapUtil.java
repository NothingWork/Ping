package com.ysu.ping.util;

import com.ysu.ping.Entity.ArpParam;
import com.ysu.ping.Entity.IcmpParam;
import com.ysu.ping.Entity.PingReply;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;
import org.pcap4j.packet.namednumber.*;
import org.pcap4j.util.ByteArrays;
import org.pcap4j.util.MacAddress;

import java.net.Inet4Address;
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
    public InetAddress getLocalIp() {
        InetAddress addr = null;
        try {
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
        } catch (Exception e) {
            return null;
        }

        return addr;
    }

    /**
     * @description: 通过ip地址获取mac地址
     * @param: ip地址
     * @return: 字符串
     */
    public String getMacAddressByIp(InetAddress addr) {
        PcapNetworkInterface nif;
        try {
            nif = Pcaps.getDevByAddress(addr);
        } catch (Exception e) {
            return null;
        }
        if (nif != null) {
            return nif.getLinkLayerAddresses().get(0).toString();
        } else return null;
    }

    /**
     * @description: 抓包
     * @param: ip地址
     * @return: void
     */
    public String getRouterMac(ArpParam arpParam) {
        String routerMac = "";
        try {
            //1.获取网络接口
            InetAddress addr = InetAddress.getByName(arpParam.getSrcIp());// 源ip
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);// 网卡接口
            //2.开启pcap抓包句柄(长度，混杂模式，超时时间)
            PcapHandle capHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            //3.设置过滤器，只接收对arp广播回应的数据包
            capHandle.setFilter("arp dst " + arpParam.getSrcIp(), BpfProgram.BpfCompileMode.OPTIMIZE);
            while (true) {
                //4.发包
                sendArp(arpParam);
                //5.抓包
                Packet packet = capHandle.getNextPacket();
                //6.获取抓包信息
                if (packet != null) {
                    //7.提取想要的信息
                    ArpPacket arpPacket = packet.get(ArpPacket.class);
                    routerMac = arpPacket.getHeader().getSrcHardwareAddr().toString();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        //8.返回结果
        return routerMac;
    }

    /**
     * @description: 发送ARP广播数据包
     * @param: 目标ip
     * @return: void
     */
    public void sendArp(ArpParam arpParam) {
        try {
            //0.获取参数和网卡接口
            InetAddress srcAddr = InetAddress.getByName(arpParam.getSrcIp()); //源ip地址
            PcapNetworkInterface nif = Pcaps.getDevByAddress(srcAddr); //网卡接口
            MacAddress srcMac = MacAddress.getByName(arpParam.getSrcMac()); //源MAC地址
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
                    .dstProtocolAddr(InetAddress.getByName(arpParam.getDstIp())); //目的IP地址
            //2.构建以太网帧数据包
            EthernetPacket.Builder etherBuilder = new EthernetPacket.Builder();
            etherBuilder
                    .dstAddr(MacAddress.ETHER_BROADCAST_ADDRESS) //目的MAC地址，广播地址
                    .srcAddr(srcMac) //源MAC地址
                    .type(EtherType.ARP) //帧类型，ARP
                    .payloadBuilder(arpBuilder) //装载的ARP数据包
                    .paddingAtBuild(true); //构建时自动填充，开启
            //3.开启发包句柄并发包
            Packet p = etherBuilder.build();
            PcapHandle sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            sendHandle.sendPacket(p);
            //4.回收资源
            sendHandle.close();
        } catch (Exception e) {
            System.out.println("错误信息-发送数据包失败");
        }

    }

    public PingReply pingIp(IcmpParam icmpParam) {
        int count = 1;//最大发包次数
        PingReply pingReply = new PingReply();
        try {
            //1.获取网络接口
            InetAddress addr = InetAddress.getByName(icmpParam.getSrcIp());// 源ip
            PcapNetworkInterface nif = Pcaps.getDevByAddress(addr);// 网卡接口
            //2.开启pcap抓包句柄(长度，混杂模式，超时时间)
            PcapHandle capHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            //3.设置过滤器，只接收来自目标ip的Icmp回显响应数据包
            capHandle.setFilter("(icmp[icmptype] == 0) and (src " + icmpParam.getDstIp() + ")", BpfProgram.BpfCompileMode.OPTIMIZE);
            while (true) {
                //4.发包
                sendIcmp(icmpParam);
                long time = System.currentTimeMillis();//发包时间
                //5.抓包
                Packet packet = capHandle.getNextPacket();
                //6.获取抓包信息
                if (packet != null) {
                    //7.提取想要的信息
                    pingReply.setTime(System.currentTimeMillis() - time);
                    IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                    pingReply.setIp(String.valueOf(ipV4Packet.getHeader().getSrcAddr()));
                    pingReply.setTtL(String.valueOf(ipV4Packet.getHeader().getTtl()));
                    break;
                }
                if(count>4){
                    pingReply.setIp("");
                    break;//发包超过4次，视为超时
                }
                count++;
            }
            //8.释放资源
            capHandle.close();
            return pingReply;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * @description: 发送Icmp回显数据包
     * @param: Icmp数据包参数
     * @return: void
     */
    public void sendIcmp(IcmpParam icmpParam) {
        try {
            //0.获取网卡接口
            InetAddress srcAddr = InetAddress.getByName(icmpParam.getSrcIp()); //源ip地址
            PcapNetworkInterface nif = Pcaps.getDevByAddress(srcAddr); //网卡接口
            //
            // 1.构建ICMP回显请求数据包
            IcmpV4EchoPacket.Builder echoBuilder = new IcmpV4EchoPacket.Builder();
            echoBuilder
                    .identifier((short) 1)// id标识码
                    .payloadBuilder(new UnknownPacket.Builder().rawData(new byte[32]));// 填充32长度的字节数组
            //2.构建ICMP普通数据包
            IcmpV4CommonPacket.Builder commonBuilder = new IcmpV4CommonPacket.Builder();
            commonBuilder
                    .type(IcmpV4Type.ECHO)// 包类型，回显数据包
                    .code(IcmpV4Code.NO_CODE)// 代码，与上一条共同指定包类型为回显数据包
                    .payloadBuilder(echoBuilder)// 装载ICMP回显数据包
                    .correctChecksumAtBuild(true);// 检验和计算，开启
            //3.构建IPV4数据包
            IpV4Packet.Builder ipv4Builder = new IpV4Packet.Builder();
            ipv4Builder
                    .version(IpVersion.IPV4) //版本，ipv4
                    .tos(IpV4Rfc791Tos.newInstance((byte) 0))//Type of Service,服务类型设为一般服务
                    .ttl((byte) 100)//生存时间
                    .protocol(IpNumber.ICMPV4)//传输层协议，icmp
                    .srcAddr((Inet4Address) srcAddr)// 源ip地址
                    .dstAddr((Inet4Address) InetAddress.getByName(icmpParam.getDstIp()))// 目的ip地址
                    .payloadBuilder(commonBuilder)// 装载ICMP普通数据包
                    .correctChecksumAtBuild(true)// 校验和计算，开启
                    .correctLengthAtBuild(true); // 校验长度，开启
            //4.构建以太网帧数据包
            EthernetPacket.Builder ethernetPacket = new EthernetPacket.Builder();
            ethernetPacket
                    .dstAddr(MacAddress.getByName(icmpParam.getDstMac()))// 目标MAC（指向路由MAC）
                    .srcAddr(MacAddress.getByName(icmpParam.getSrcMac()))// 源MAC
                    .type(EtherType.IPV4)// 帧类型，ipv4
                    .payloadBuilder(ipv4Builder)// 装载ipv4数据包
                    .paddingAtBuild(true); // 构建时自动填充，开启
            //5.开启发包句柄并发包
            Packet p = ethernetPacket.build();
            PcapHandle sendHandle = nif.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            sendHandle.sendPacket(p);
            //6.回收资源
            sendHandle.close();
        } catch (Exception e) {
            System.out.println("发送ICMP数据包时出错！");
        }
    }

}
