package com.hncs.world.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说预览返回类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookPreviewVo {
    private String chapterName;//章节标题

    private String chapterLink;
}
