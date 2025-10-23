package com.hncs.world.pojo.vo;

import lombok.Data;

/**
 * 登录返回类
 */
@Data
public class LoginVo {
    private UserVo user;
    private TokenVo tokens;
}