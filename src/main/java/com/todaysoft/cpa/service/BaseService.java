package com.todaysoft.cpa.service;

import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.param.CPA;
import com.todaysoft.cpa.merge.MergeInfo;
import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.ExceptionInfo;
import com.todaysoft.cpa.utils.JsoupUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:35
 */
public abstract class BaseService {
    private static Logger logger= LoggerFactory.getLogger(BaseService.class);
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
    protected boolean saveOne(CPA cpa, MergeInfo mergeInfo, String id, int status) {
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
                logger.info("【Merge-"+ cpa.name()+"】插入数据库成功,id="+id);
            }
        }catch (Exception e){
            cpa.dbId.remove(id);
            if (e instanceof DataException){
                logger.error("【MergeException】存入数据异常，info:["+cpa.name()+"]-->"+id+",cause:"+e.getMessage());
            } else {
                logger.error("【MergeException】存入数据异常，info:["+cpa.name()+"]-->"+id);
                logger.error("【MergeException】"+ ExceptionInfo.getErrorInfo(e));
            }
        }
        if (success){
            mergeInfo.sign.remove(id);
            mergeInfo.mergeList.removeIf(strings ->strings.get(0).equals(id));
        }
        return success;
    }

}
