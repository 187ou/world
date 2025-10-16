package com.hncs.world.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 统一返回List包装结果
 */
@Data
@AllArgsConstructor
public class UnifyList<T> {
    private Integer ListSize;

    private List<T> list;
}
