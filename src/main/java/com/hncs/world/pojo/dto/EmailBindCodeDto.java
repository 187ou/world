package com.hncs.world.pojo.dto;

import lombok.Data;
import javax.validation.constraints.*;
@Data
public class EmailBindCodeDto {
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String newEmail;
}