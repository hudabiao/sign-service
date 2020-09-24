package com.daibiao.signapp.enumerate;

/**
 * SignStatusEnum
 *
 * @description 签名状态
 * @author hudaibiao-1
 * @date 2020/9/23 20:35
 * @version v1.0.0
 */
public enum SignStatusEnum {

    /**未签名*/
    WQM("01", "未签名"),
    /**已签名*/
    YQM("02", "已签名");

    /**代码*/
    private String code;
    /**名称*/
    private String name;

    SignStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public String getCode(){
        return code;
    }
}
