package com.hncs.world.pojo.dto;

import lombok.Data;

@Data
public class UserPurchasedBookDto {
    private String bookName;
    private String bookLink;
    private Integer cost;
}
