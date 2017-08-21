package com.todaysoft.cpa.param;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 11:38
 */
public class Param {
    public static LinkedBlockingQueue<ContentParam> CONTENT_QUEUE;
    public static LinkedBlockingQueue<ContentParam> FAILURE_QUEUE;
    public static String AUTHORIZATION;
}
