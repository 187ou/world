package com.hncs.world.pojo.vo;

import lombok.Data;
/**
 * 注册返回类
 */
@Data
public class RegisterVo {
    private Long userId;
    private String userName;
    private String nickName;
    private String email;
}