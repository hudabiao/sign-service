package com.daibiao.signapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Contract
 *
 * @description 合同
 * @author hudaibiao-1
 * @date 2020/9/23 20:40
 * @version v1.0.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "contract")
public class Contract {

    /**合同id*/
    @Id
    private String id;
    /**名称*/
    private String name;
    /**用户id*/
    private String userId;
    /**状态*/
    private String status;
    /**创建时间*/
    private Date createDatetime;
    /**更新时间*/
    private Date updateDatetime;
}
