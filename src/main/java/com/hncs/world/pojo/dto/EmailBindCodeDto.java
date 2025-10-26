package com.hncs.world.pojo.dto;

import lombok.Data;
import javax.validation.constraints.*;
@Data
public class EmailBindCodeDto {
    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过100位")
    private String newEmail;
}