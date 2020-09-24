package com.daibiao.signapp.enumerate;

/**
 * RoleEnum
 *
 * @description 角色-枚举
 * @author hudaibiao-1
 * @date 2020/9/23 20:34
 * @version v1.0.0
 */
public enum RoleEnum {
    /**管理员*/
    GLY("01", "管理员"),
    /**学员*/
    XY("02", "学员");

    /**代码*/
    private String code;

    /**名称*/
    private String name;

    RoleEnum(String code, String name) {
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
