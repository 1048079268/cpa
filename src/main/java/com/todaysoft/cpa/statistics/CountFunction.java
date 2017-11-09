package com.todaysoft.cpa.statistics;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/31 17:54
 */
@FunctionalInterface
public interface CountFunction {
    Map<String,Long> count(JSONObject json, Map<String,Long> map);
}
