package com.ysu.ping.Service;

import com.ysu.ping.Entity.ArpParam;
import com.ysu.ping.Entity.IcmpParam;
import com.ysu.ping.Entity.Result;

/**
 * @author Yun
 * @description: Pcap服务层
 */
public interface PcapService {
     Result getRouterMac(ArpParam arpParam);
     Result getLocalInfo();
     Result pingIp(IcmpParam icmpParam);
}
