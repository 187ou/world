package com.hncs.world.pojo.vo;

import lombok.Data;

@Data
public class RegisterVo {
    private Long userId;
    private String userName;
    private String nickName;
    private String email;
}