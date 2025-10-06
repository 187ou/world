package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 打开，阅读小说返回类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenBookVo {
    private String chapterName;
    private String chapterTxt;
    private String chapterTxtSize;
}
