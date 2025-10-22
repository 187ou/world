package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 热门书籍统计表
 * @TableName hot
 */
@TableName(value ="hot")
@Data
public class Hot {
    /**
     * 书籍名称（主键）
     */
    @TableId
    private String bookName;

    /**
     * 热度计数（如点击量、收藏量）
     */
    private Integer cnt;
}