package com.todaysoft.cpa.param;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @desc: 全局变量
 * @author: 鱼唇的人类
 * @date: 2017/8/15 11:38
 */
public class GlobalVar {
    /**
     * 是否可以发送JSON结构改变的异常信息的邮件,也是判断是否关闭线程的标识
     */
    public final static AtomicBoolean SEND_STRUCTURE_EMAIL=new AtomicBoolean(true);
    private static LinkedBlockingQueue<ContentParam> CONTENT_QUEUE;
    private static LinkedBlockingQueue<ContentParam> DRUG_QUEUE;
    private static LinkedBlockingQueue<ContentParam> FAILURE_QUEUE;
    private static String AUTHORIZATION;
    /**
     * 异常过滤列表
     * 已在过滤列表中的异常不再输出至错误日志文件
     * @see com.todaysoft.cpa.utils.DataException
     */
    public final static Set<String> EXCEPTION_FILTER_DATA=new HashSet<>();

    public static LinkedBlockingQueue<ContentParam> getContentQueue() {
        return CONTENT_QUEUE;
    }

    public static void setContentQueue(LinkedBlockingQueue<ContentParam> contentQueue) {
        CONTENT_QUEUE = contentQueue;
    }

    public static LinkedBlockingQueue<ContentParam> getFailureQueue() {
        return FAILURE_QUEUE;
    }

    public static void setFailureQueue(LinkedBlockingQueue<ContentParam> failureQueue) {
        FAILURE_QUEUE = failureQueue;
    }

    public static LinkedBlockingQueue<ContentParam> getDrugQueue() {
        return DRUG_QUEUE;
    }

    public static void setDrugQueue(LinkedBlockingQueue<ContentParam> drugQueue) {
        DRUG_QUEUE = drugQueue;
    }

    public static String getAUTHORIZATION() {
        return AUTHORIZATION;
    }

    public static void setAUTHORIZATION(String AUTHORIZATION) {
        GlobalVar.AUTHORIZATION = AUTHORIZATION;
    }
}
