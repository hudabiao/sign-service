package com.daibiao.signapp.util;

import com.daibiao.signapp.model.Result;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.util.ResultUtil
 * @description ResultUtil
 * @date 2020-03-20 17:01:00
 */
public class ResultUtil {

    /**
     * ResultUtil
     *
     * @description 返回成功消息
     * @author hudaibiao-1
     * @param  data 数据
     * @date 2020/9/23 20:20
     * @return 结果
     * @version v1.0.0
     */
    public static Result success(Object data){
        return new Result("01","成功",data);
    }

    /**
     * ResultUtil
     *
     * @description 返回失败消息
     * @author hudaibiao-1
     * @date 2020/9/23 20:20
     * @return 结果
     * @version v1.0.0
     */
    public static Result fail(){
        return new Result("02","失败",null);
    }
}
