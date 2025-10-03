package com.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 章节实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    private Integer chapterId;

    private String headLine;

    private String txt;

    private Integer size;

    private Integer curNumber;

    private String chapterLink;
}
