package com.daibiao.signapp.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * FileNameEnum
 *
 * @author hudaibiao-1
 * @version v1.0.0
 * @description 文件名常量
 * @date 2020-09-23 20:55:00
 */
@AllArgsConstructor
@Getter
public enum FileNameEnum {

    /**未签名*/
    UNSIGNED_FILENAME("contract_unsigned.pdf"),
    /**已签名*/
    SIGNED_FILENAME("contract_signed.pdf");

    /**名称*/
    private String name;
}
