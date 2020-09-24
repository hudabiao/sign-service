package com.daibiao.signapp.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.model.User
 * @description 用户
 * @date 2020-03-18 17:16:00
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;
    /**登录id*/
    private String loginId;
    /**用户名*/
    private String username;
    /**电话*/
    private String phone;
    /**密码*/
    private String password;
    /**角色*/
    private String role;
    /**创建时间*/
    private Date createDatetime;
    /**更新时间*/
    private Date updateDatetime;
    /**合同列表*/
    @OneToMany(targetEntity = Contract.class,mappedBy = "userId",cascade = CascadeType.ALL)
    private List<Contract> contractList;

}
