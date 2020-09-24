package com.daibiao.signapp.util;

import java.util.UUID;

/**
 * UUIDUtil
 *
 * @description UUIDUtil
 * @author hudaibiao-1
 * @date 2020/9/23 20:25
 * @version v1.0.0
 */
public class UUIDUtil {

    /**
     * UUIDUtil
     *
     * @description 生成UUID
     * @author hudaibiao-1
     * @date 2020/9/23 20:26
     * @return UUID
     * @version v1.0.0
     */
    public static String generateId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
