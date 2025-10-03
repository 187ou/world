package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节返回类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterVo {
    private String chapterName;//章节标题

    private String chapterLink;
}
