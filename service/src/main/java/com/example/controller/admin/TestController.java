package com.example.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "测试接口")
public class TestController {

    @GetMapping("/test")
    @Operation(summary = "健康检查")
    public String test() {
        return "OK";
    }
}