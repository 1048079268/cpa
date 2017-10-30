package com.todaysoft.cpa.utils.JsonConverter;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc: 将jsonObject转换为实体的函数接口定义
 * @author: 鱼唇的人类
 * @date: 2017/10/26 11:07
 */
@FunctionalInterface
public interface JsonObjectKeyConverter<T> {
    T convert(JSONObject object,String key);
}
