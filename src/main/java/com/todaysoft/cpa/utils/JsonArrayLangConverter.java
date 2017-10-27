package com.todaysoft.cpa.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @desc: 将jsonArray转换为集合的函数接口定义（添加语言参数）
 * @author: 鱼唇的人类
 * @date: 2017/10/27 11:52
 */
@FunctionalInterface
public interface JsonArrayLangConverter<T> {
    List<T> convert(JSONObject object,int lang) throws InterruptedException;
}
