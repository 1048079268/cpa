package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:35
 */
public interface BaseService {
    boolean save(JSONObject object) throws InterruptedException;
    boolean saveByDependence(JSONObject object,String dependenceKey);
    void initDB();
}
