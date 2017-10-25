package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:35
 */
public abstract class BaseService {
    /**
     * 保存数据
     * @param object
     * @return
     * @throws InterruptedException
     */
    public abstract boolean save(JSONObject object) throws InterruptedException;

    /**
     * 保存基于依赖的数据
     * @param object
     * @param dependenceKey
     * @return
     */
    public abstract boolean saveByDependence(JSONObject object, String dependenceKey);

    /**
     * 初始化程序
     * @throws FileNotFoundException
     */
    public abstract void initDB() throws FileNotFoundException;
}
