package com.hncs.world.pojo.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserRegisterDto {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20位之间") // 补充长度约束
    private String userName;

    private String nickName; // 可选字段，无需@NotBlank

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间") // 补充最大长度约束
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Size(min = 6, max = 6, message = "验证码必须为6位") // 补充验证码长度约束
    private String code;

}