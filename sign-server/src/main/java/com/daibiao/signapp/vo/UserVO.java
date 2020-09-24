package com.daibiao.signapp.vo;

import com.daibiao.signapp.model.User;

import lombok.Data;

/**
 * UserVO
 *
 * @description UserVO
 * @author hudaibiao-1
 * @date 2020/9/23 20:29
 * @version v1.0.0
 */
@Data
public class UserVO extends User {
    /**token*/
    private String token;
}
