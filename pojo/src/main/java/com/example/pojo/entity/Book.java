package com.example.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小说实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer userId;

    private String bookName;

    private String bookLink;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String updateUser;

    private String createUser;

    //会否收藏判断
    private Integer isCollect;

    //是否最近判断
    private Integer isRecently;

    //小说章节id
    private Integer chapterId;
}
