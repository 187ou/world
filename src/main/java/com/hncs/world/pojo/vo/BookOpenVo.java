package com.hncs.world.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 打开，阅读小说返回类
 **/
@Data
public class BookOpenVo {
    private String chapterName;
    private String chapterTxt;
    private String chapterTxtSize;
}
