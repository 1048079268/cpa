package com.todaysoft.cpa.utils;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/15 10:14
 */
public class MergeException extends RuntimeException {
    public MergeException() {
        super();
    }

    public MergeException(String message) {
        super(message);
    }

    public MergeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MergeException(Throwable cause) {
        super(cause);
    }

    protected MergeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
