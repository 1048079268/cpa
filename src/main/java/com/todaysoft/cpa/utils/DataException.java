package com.todaysoft.cpa.utils;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/31 12:48
 */
public class DataException extends RuntimeException {
    public DataException() {
        super();
    }

    public DataException(String message) {
        super(message);
    }

    public DataException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataException(Throwable cause) {
        super(cause);
    }

    public DataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
