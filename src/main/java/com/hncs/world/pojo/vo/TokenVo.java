package com.hncs.world.pojo.vo;

import lombok.Data;

@Data
public class TokenVo {
    private String accessToken;
    private Integer expiresIn;
}