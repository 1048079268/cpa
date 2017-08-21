package com.todaysoft.cpa.utils;

import com.alibaba.fastjson.JSONArray;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/14 11:21
 */
public class JsonUtil {
    /**
     * @desc: 将json数组拼成字符串
     * @author: 鱼唇的人类
     * @param jsonArray
     * @return
     */
    public static String jsonArrayToString(JSONArray jsonArray){
        String rel="";
        if (jsonArray!=null&&jsonArray.size()>0){
            for (int i=0;i<jsonArray.size();i++){
                rel=rel+jsonArray.getString(i);
            }
        }
        return rel;
    }
}
