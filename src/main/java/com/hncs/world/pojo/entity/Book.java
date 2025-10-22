package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;
import lombok.Data;

/**
 * 书籍信息表
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book {
    /**
     * 主键ID（自增）
     */
    @TableId(type = IdType.AUTO)
    private Long bookId;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 书籍链接
     */
    private String bookLink;

    /**
     * 创建时间（自动填充）
     */
    private Date createTime;

    /**
     * 更新时间（自动更新）
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 是否收藏（1=收藏，0=未收藏）
     */
    private Integer isCollect;

    /**
     * 阅读信息令牌
     */
    private String token;

    /**
     * 逻辑删除（1=已删除，0=未删除）
     */
    @TableLogic
    private Integer isDeleted;
}