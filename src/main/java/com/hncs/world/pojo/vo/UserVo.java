package com.hncs.world.pojo.vo;

import lombok.Data;

import java.util.Date;
/**
 * 用户信息返回类
 */
@Data
public class UserVo {
    private Long userId;
    private String userName;
    private String nickName;
    private String email;
    private String phone;
    private Integer sex;
    private Integer level;
    private Integer money;
    private Integer status;
    private Date createTime;
}