package com.hncs.world.pojo.vo;

import lombok.Data;
/**
 * 发送验证码返回类
 */
@Data
public class SendCodeVo {
    private String email;
    private Integer expiresIn;
}