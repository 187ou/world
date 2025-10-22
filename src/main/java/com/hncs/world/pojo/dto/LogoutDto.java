package com.hncs.world.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LogoutDto {
    @NotBlank(message = "refreshToken不能为空")
    private String refreshToken;
}