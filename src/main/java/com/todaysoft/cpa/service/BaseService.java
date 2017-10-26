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
     * @param en
     * @param cn
     * @return
     * @throws InterruptedException
     */
    public abstract boolean save(JSONObject en,JSONObject cn) throws InterruptedException;

    /**
     * 保存基于依赖的数据
     * @param en
     * @param cn
     * @param dependenceKey
     * @return
     */
    public abstract boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey) throws InterruptedException;

    /**
     * 初始化程序
     * @throws FileNotFoundException
     */
    public abstract void initDB() throws FileNotFoundException;
}
