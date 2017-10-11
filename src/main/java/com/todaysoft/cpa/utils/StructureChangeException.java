package com.todaysoft.cpa.utils;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/10 16:38
 */
public class StructureChangeException extends Exception{
    public StructureChangeException() {
        super();
    }

    public StructureChangeException(String message) {
        super(message);
    }

    public StructureChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StructureChangeException(Throwable cause) {
        super(cause);
    }

    protected StructureChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
