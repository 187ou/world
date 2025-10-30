package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户阅读记录表
 * @TableName read
 */
@TableName(value ="`read`")
@Data
public class Read {
    /**
     * 阅读标识令牌（主键）
     */
    @TableId
    private String token;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 书籍链接
     */
    private String bookLink;

    /**
     * 章节链接
     */
    private String chapterLink;

    /**
     * 章节序号/阅读进度
     */
    private Integer number;
}