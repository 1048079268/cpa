package com.todaysoft.cpa.param;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 11:38
 */
public class Param {
    private static LinkedBlockingQueue<ContentParam> CONTENT_QUEUE;
    private static LinkedBlockingQueue<ContentParam> FAILURE_QUEUE;
    private static String AUTHORIZATION;

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

    public static String getAUTHORIZATION() {
        return AUTHORIZATION;
    }

    public static void setAUTHORIZATION(String AUTHORIZATION) {
        Param.AUTHORIZATION = AUTHORIZATION;
    }
}
