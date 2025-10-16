package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 用户书籍购买记录表
 * @TableName purchased
 */
@TableName(value ="purchased")
@Data
public class Purchased {
    /**
     * 用户ID（联合主键）
     */
    @TableId
    private Long userId;

    /**
     * 书籍ID（联合主键）
     */
    private Long bookId;

    /**
     * 书籍链接
     */
    private String bookLink;

    /**
     * 购买时间（对应LocalDateTime）
     */
    private Date purchasedTime;
}