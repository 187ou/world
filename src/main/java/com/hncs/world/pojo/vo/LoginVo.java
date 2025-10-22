package com.hncs.world.pojo.vo;

import lombok.Data;

@Data
public class LoginVo {
    private UserVo user;
    private TokenVo tokens;
}