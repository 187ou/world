package com.hncs.world.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 搜索小说返回类
 **/
@Data
public class BookSearchVo {
    private String bookName;

    private String bookLink;
}
