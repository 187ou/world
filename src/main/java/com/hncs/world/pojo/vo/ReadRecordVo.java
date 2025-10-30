package com.hncs.world.pojo.vo;

import lombok.Data;

/**
 * 小说记录返回类
 **/
@Data
public class ReadRecordVo {
    private String bookName;
    private String bookLink;
    private String chapterLink;
    private int number;
}
