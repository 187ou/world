package com.hncs.world.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户阅读样式设置表
 * @TableName level
 */
@TableName(value ="level")
@Data
public class Level {
    /**
     * 设置ID/关联等级（主键）
     */
    @TableId
    private Integer level;

    /**
     * 打印设置（1=启用，0=禁用）
     */
    private Integer print;

    /**
     * 缩小比例（如5=缩小5%）
     */
    private Integer reduce;

    /**
     * 放大比例（如10=放大10%）
     */
    private Integer enlarge;

    /**
     * 字体类型（枚举值：1=宋体，2=黑体等）
     */
    private Integer font;

    /**
     * 字体大小（如12=12号字）
     */
    private Integer fontSize;

    /**
     * 是否加粗（1=是，0=否）
     */
    private Integer bold;

    /**
     * 是否斜体（1=是，0=否）
     */
    private Integer slash;

    /**
     * 是否下划线（1=是，0=否）
     */
    private Integer underline;

    /**
     * 是否删除线（1=是，0=否）
     */
    private Integer strikethrough;

    /**
     * 文字颜色（色值编码，如0x000000=黑色）
     */
    private Integer textColor;

    /**
     * 背景色（色值编码，如0xFFFFFF=白色）
     */
    private Integer backgroundColor;

    /**
     * 样式（枚举值：1=默认，2=夜间模式等）
     */
    private Integer style;

    /**
     * 行间距（如1=单倍，2=双倍）
     */
    private Integer lineSpacing;
}