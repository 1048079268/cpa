package com.todaysoft.cpa.utils;

import java.util.Arrays;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/10 10:16
 */
public class ExceptionInfo {
    /**
     * @desc: 错误信息转换为字符串
     * @author: 鱼唇的人类
     * @param e
     * @return
     */
    public static StringBuffer getErrorInfo(Exception e){
        StackTraceElement []elements=e.getStackTrace();
        StringBuffer error=new StringBuffer();
        error.append(e.getMessage());
        Arrays.stream(elements).forEach((element)->{
            error.append("\n\t"+element.toString());
        });
        return error;
    }
}
