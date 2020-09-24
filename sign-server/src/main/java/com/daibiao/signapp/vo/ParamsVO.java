package com.daibiao.signapp.vo;

import lombok.Data;

/**
 * ParamsVO
 *
 * @description 查询查询实体
 * @author hudaibiao-1
 * @date 2020/9/23 20:27
 * @version v1.0.0
 */
@Data
public class ParamsVO {
    /**页数*/
    private Integer page;
    /**条数*/
    private Integer size;
    /**参数*/
    private String entity;
}
