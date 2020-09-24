package com.daibiao.signapp.vo;

import lombok.*;

/**
 * SignData
 *
 * @description 签名数据
 * @author hudaibiao-1
 * @date 2020/9/23 20:28
 * @version v1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignData {
    /**图片的base64编码数据*/
    private String base64ImgData;
    /**登录id*/
    private String loginId;
    /**合同id*/
    private String contractId;
}
