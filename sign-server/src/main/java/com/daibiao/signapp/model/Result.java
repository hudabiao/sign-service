package com.daibiao.signapp.model;

import lombok.*;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.model.Result
 * @description 响应结果
 * @date 2020-03-18 18:54:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result<T> {
    /**状态*/
    private String code;
    /**消息*/
    private String msg;
    /**数据*/
    private T data;
}
