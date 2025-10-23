package com.hncs.world.pojo.vo;

import lombok.Data;
/**
 * token返回类
 */
@Data
public class TokenVo {
    private String accessToken;
    private Integer expiresIn;
}