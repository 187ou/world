package com.hncs.world.pojo.vo;

import lombok.Data;

import java.util.Date;

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