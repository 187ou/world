package com.example.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小说接收类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String bookName;
    private String bookLink;
}
