package com.hncs.world.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BookVo {
    private String bookName;
    private String bookLink;
    private Date updateTime;
}
