package com.ysu.ping.controller;

import com.ysu.ping.Entity.ArpParam;
import com.ysu.ping.Entity.IcmpParam;
import com.ysu.ping.Entity.Result;
import com.ysu.ping.Service.PcapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yun
 * @description: Pcap控制层
 */
@Tag(name = "Pcap操作")
@RestController
public class PcapController {
    @Autowired
    private PcapService pcapService;
    @Operation(summary = "获取路由Mac")
    @GetMapping("/getRouterMac")
    public Result getRouterMac(@RequestParam("dstIp") String dstIp,
                               @RequestParam("srcIp") String srcIp,
                               @RequestParam("srcMac") String srcMac
                               ) {
        return pcapService.getRouterMac(new ArpParam(dstIp,srcIp,srcMac));
    }

    @Operation(summary = "获取本机信息")
    @GetMapping("/getLocalInfo")
    public Result getLocalInfo(){
        return pcapService.getLocalInfo();
    }

    @Operation(summary = "ping")
    @GetMapping("/pingIp")
    public Result pingIp(
            @RequestParam("srcIp") String srcIp,
            @RequestParam("srcMac") String srcMac,
            @RequestParam("dstIp") String dstIp,
            @RequestParam("dstMac") String dstMac
    ){
        return pcapService.pingIp(new IcmpParam(srcIp,srcMac,dstIp,dstMac));
    }
}
