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
        StringBuffer stringBuffer=new StringBuffer();
        if (jsonArray!=null&&jsonArray.size()>0){
            for (int i=0;i<jsonArray.size();i++){
                stringBuffer.append(jsonArray.getString(i));
            }
        }
        return stringBuffer.toString();
    }

    /**
     * @desc: 将json数组拼成字符串
     * @author: 鱼唇的人类
     * @param jsonArray
     * @return
     */
    public static String jsonArrayToString(JSONArray jsonArray,String separator){
        StringBuffer stringBuffer=new StringBuffer();
        if (jsonArray!=null&&jsonArray.size()>0){
            for (int i=0;i<jsonArray.size();i++){
                if (i==0){
                    stringBuffer.append(jsonArray.getString(i));
                }else {
                    stringBuffer.append(separator+jsonArray.getString(i));
                }
            }
        }
        return stringBuffer.toString();
    }
}
