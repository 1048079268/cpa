package com.todaysoft.cpa.statistics;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/11/2 14:39
 */
@FunctionalInterface
public interface JudgeNeedCount {
    boolean judge(JSONObject json);
}
