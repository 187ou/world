package com.hncs.world.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserLoginDto {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名/邮箱长度必须在2-20位之间")
    // 说明：支持用户名或邮箱登录，统一限制长度范围
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;
}