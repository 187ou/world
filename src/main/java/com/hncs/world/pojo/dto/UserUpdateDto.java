package com.hncs.world.pojo.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

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
}