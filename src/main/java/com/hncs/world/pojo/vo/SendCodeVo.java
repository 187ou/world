package com.hncs.world.pojo.vo;

import lombok.Data;

@Data
public class SendCodeVo {
    private String email;
    private Integer expiresIn;
}