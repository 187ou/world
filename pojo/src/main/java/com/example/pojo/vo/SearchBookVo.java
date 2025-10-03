package com.example.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索小说返回类
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookVo {
    private String bookName;

    private String bookLink;
}
