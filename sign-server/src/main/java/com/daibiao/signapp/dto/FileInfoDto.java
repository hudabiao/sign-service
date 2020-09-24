package com.daibiao.signapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Contract
 *
 * @description 文件信息
 * @author hudaibiao-1
 * @date 2020/9/23 20:40
 * @version v1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoDto {
    /**登录id*/
    private String loginId;
    /**名称*/
    private String name;
    /**合同id*/
    private String contractId;
    /**状态*/
    private String status;
}
