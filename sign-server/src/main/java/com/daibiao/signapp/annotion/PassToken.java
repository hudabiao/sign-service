package com.daibiao.signapp.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.annotion.PassToken
 * @description 跳过验证
 * @date 2020-03-18 17:06:00
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
    boolean required() default true;
}
