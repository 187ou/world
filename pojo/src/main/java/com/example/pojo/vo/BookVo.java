package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 小说返回类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookVo {
    private String bookName;

    private String bookLink;

    private LocalDateTime updateTime;
}
