package com.todaysoft.cpa.service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.utils.JsoupUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:35
 */
public abstract class BaseService<T> {
    /**
     * 保存数据
     * @param en
     * @param cn
     * @param status
     *    0 表示如果与老库有重合的话就终止该次运行并写入待审核列表
     *    1 表示如果与老库有重合的话就与老库合并
     *    2 表示如果与老库有重合的话也不与老库合并
     * @return
     * @throws InterruptedException
     */
    public abstract boolean save(JSONObject en,JSONObject cn,int status) throws InterruptedException;

    /**
     * 保存基于依赖的数据
     * @param en
     * @param cn
     * @param status
     *    0 表示如果与老库有重合的话就终止运行并并写入待审核列表
     *    1 表示如果与老库有重合的话就与老库合并
     *    2 表示如果与老库有重合的话也不与老库合并
     * @param dependenceKey
     * @return
     */
    public abstract boolean saveByDependence(JSONObject en,JSONObject cn, String dependenceKey,int status) throws InterruptedException;

    /**
     * 初始化数据
     * @throws FileNotFoundException
     */
    public abstract void initDB() throws FileNotFoundException;
    /**
     * 单独抓取和保存某个id
     * @param cpa
     * @param id
     * @return
     * @throws IOException
     */
    protected boolean saveOne(CPA cpa,String id,int status) {
        JSONObject en = null;
        JSONObject cn = null;
        try {
            en = JsoupUtil.getJsonByUrl(cpa, id, "en");
            cn = JsoupUtil.getJsonByUrl(cpa, id, "zn");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (en==null){
            return false;
        }
        if (cn==null){
            cn=en;
        }
        boolean success=false;
        try {
            if (cpa.dbId.add(id)){
                success=save(en,cn,status);
                if (!success){
                    cpa.dbId.remove(id);
                }
            }
        }catch (Exception e){
            cpa.dbId.remove(id);
        }
        return success;
    }

}
