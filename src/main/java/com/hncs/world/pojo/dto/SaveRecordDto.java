package com.hncs.world.pojo.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SaveRecordDto {
    @NotBlank(message = "bookName不能为空")
    private String bookName;
    @NotBlank(message = "chapterLink不能为空")
    private String chapterLink;
    @NotBlank(message = "bookLink不能为空")
    private String bookLink;
    @NotNull(message = "章节序号不能为空") // 整数用 @NotNull 校验非 null
    @Min(value = 1, message = "章节序号必须大于0") // 可选：校验最小值（如至少为1）
    private int number;
}
