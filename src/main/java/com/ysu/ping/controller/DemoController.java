package com.ysu.ping.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yun
 * @description: 测试
 */
@Tag(name = "测试")
@RestController
public class DemoController {
    @Operation(summary = "测试")
    @GetMapping("/test")
    public void test(@RequestParam("str") String str) {
        System.out.println(str);
    }
}
