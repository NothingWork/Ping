package com.ysu.ping.controller;

import com.ysu.ping.Entity.ArpParam;
import com.ysu.ping.Entity.IcmpParam;
import com.ysu.ping.Entity.Result;
import com.ysu.ping.Service.PcapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/getRouterMac")
    @ResponseBody
    public Result getRouterMac(@RequestBody ArpParam arpParam) {
        System.out.println(arpParam.getSrcIp());
        return pcapService.getRouterMac(arpParam);
    }

    @Operation(summary = "获取本机信息")
    @GetMapping("/getLocalInfo")
    public Result getLocalInfo(){
        return pcapService.getLocalInfo();
    }

    @Operation(summary = "ping")
    @PostMapping("/pingIp")
    @ResponseBody
    public Result pingIp(
            @RequestBody IcmpParam icmpParam
    ){
        return pcapService.pingIp(icmpParam);
    }
}
