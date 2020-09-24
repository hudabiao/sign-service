package com.daibiao.signapp.exception;

/**
 * @author hudaibiao-1
 * @version 1.0.0
 * @ClassName com.daibiao.signapp.exception.VerifyTokenException
 * @description 基础异常
 * @date 2020-03-18 17:13:00
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }
}
