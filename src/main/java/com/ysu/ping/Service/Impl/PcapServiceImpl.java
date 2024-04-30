package com.ysu.ping.Service.Impl;

import com.ysu.ping.Entity.*;
import com.ysu.ping.Service.PcapService;
import com.ysu.ping.util.PcapUtil;
import org.springframework.stereotype.Service;

import java.net.InetAddress;


/**
 * @author Yun
 * @description: Pcap服务层接口实现
 */
@Service
public class PcapServiceImpl implements PcapService {
    PcapUtil pcapUtil = new PcapUtil();

    /**
     * @description: 获取路由的MAC地址
     * @param:Arp数据包填写参数
     * @return: com.ysu.ping.Entity.Result
     */
    @Override
    public Result getRouterMac(ArpParam arpParam) {
        String routerMac = null;
        try{
             routerMac = pcapUtil.getRouterMac(arpParam);
        }catch (Exception e){
            //出现异常
            return new Result(401,"error",null);
        }
        if(routerMac != null){
            //获取成功
            return new Result(101,"success",routerMac);
        }
        else return new Result(201,"failed",null);
    }

    /**
     * @description: 获取本机ip和MAC信息
     * @param:
     * @return: com.ysu.ping.Entity.Result
     */
    @Override
    public Result getLocalInfo() {
        InetAddress localIp = pcapUtil.getLocalIp();
        String localMac = pcapUtil.getMacAddressByIp(localIp);
        if(localMac!=null && localIp!=null){
            //获取成功
            return new Result(102,"success",new LocalInfo(localIp.toString().substring(1),localMac));
        }
        else return new Result(202,"failed",null);
    }

    @Override
    public Result pingIp(IcmpParam icmpParam) {
        PingReply pingReply = pcapUtil.pingIp(icmpParam);
        if(pingReply!=null){
            if(!pingReply.getIp().equals("")){
                //ping成功
                return new Result(103,"success",pingReply);
            }
            else{
                //ping超时
                return new Result(203,"timeout",null);
            }
        }
        else{
            //ping操作异常
            return new Result(402,"error",null);
        }
    }
}
