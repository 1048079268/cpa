package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:35
 */
public abstract class BaseService {
    public abstract boolean save(JSONObject object) throws InterruptedException;
    public abstract boolean saveByDependence(JSONObject object, String dependenceKey);
    public abstract void initDB() throws FileNotFoundException;
}
