package com.todaysoft.cpa.utils.JsonConverter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @desc: 将jsonArray转换为集合的函数接口定义
 * @author: 鱼唇的人类
 * @date: 2017/10/26 11:10
 */
@FunctionalInterface
public interface JsonArrayConverter<T> {
    List<T> convert(JSONObject object) throws InterruptedException;
}
