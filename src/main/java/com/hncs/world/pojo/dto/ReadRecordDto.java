package com.hncs.world.pojo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReadRecordDto {
    @NotBlank(message = "bookLink不能为空")
    private String bookLink;
}
