package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 用户信息表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User {
    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 登录用户名
     */
    private String userName;

    /**
     * 用户密码（加密存储）
     */
    private String password;

    /**
     * 性别（1=男，2=女，0=未知）
     */
    private Integer sex;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phone;

    /**
     * 用户等级（默认1级）
     */
    private Integer level;

    /**
     * 账户余额（单位：分，避免浮点数精度问题）
     */
    private Integer money;

    /**
     * 账号状态（1=正常，0=禁用）
     */
    private Integer status;

    /**
     * 逻辑删除（1=已删除，0=未删除）
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 创建时间（自动填充）
     */
    private Date createTime;

    /**
     * 更新时间（自动更新）
     */
    private Date updateTime;
}