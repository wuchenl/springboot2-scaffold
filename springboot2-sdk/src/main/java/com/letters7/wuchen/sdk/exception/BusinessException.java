package com.letters7.wuchen.sdk.exception;

import com.letters7.wuchen.springboot2.utils.exception.UtilException;

/**
 * @author wuchen
 * @version 0.1
 * @date 2018/12/29 10:13
 * @desc 表明这是一个业务异常
 */
public class BusinessException extends RuntimeException {

    protected int status = 400;

    public BusinessException(int status, String message) {
        super(message);
        this.status = status;
    }

    public BusinessException(String message) {
        super(message);
        this.status = 400;
    }

    public BusinessException(Throwable throwable) {
        super(UtilException.getBootMessage(throwable));
        this.status = 400;
    }

    public BusinessException(String message, Throwable throwable, int status) {
        super(message, throwable);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
