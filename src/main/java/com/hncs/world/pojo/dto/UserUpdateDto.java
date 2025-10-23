package com.hncs.world.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserUpdateDto {
    @NotNull(message = "用户ID不能为空")
    private Long userId;         // 用户唯一标识（必传，用于定位用户）

    @Size(min = 1, max = 20, message = "昵称长度必须在1-20位之间") // 可选字段，有值时校验长度
    private String nickName;     // 可选：用户昵称

    @Min(value = 0, message = "性别参数无效（0=未知，1=男，2=女）")
    @Max(value = 2, message = "性别参数无效（0=未知，1=男，2=女）")
    private Integer sex;         // 可选：性别（有值时校验范围）

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确") // 可选字段，有值时校验格式
    private String phone;        // 可选：手机号

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50位") // 可选字段，有值时校验格式和长度
    private String email;        // 可选：邮箱

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须为6位数字")
    @ApiModelProperty(value = "邮箱换绑验证码（仅修改邮箱时必填，6位数字）", required = false)
    private String emailVerificationCode; // 新邮箱的验证码

    // 新增：邮箱验证码（仅当email不为空时必填）
    @AssertTrue(message = "修改邮箱时必须提供6位数字验证码")
    public boolean isEmailVerificationCodeValid() {
        if (email == null) {
            return true;
        } else {
            // 同时校验“非空”和“6位数字格式”
            return emailVerificationCode != null
                    && !emailVerificationCode.isEmpty()
                    && emailVerificationCode.matches("^\\d{6}$");
        }
    }
}