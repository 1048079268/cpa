package com.todaysoft.cpa.compare;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/10 11:54
 */
public class AcquireJsonStructure {
    public static Map<String,JsonDataType> getJsonKeyMap(String path) throws FileNotFoundException {
        JSONObject jsonObject=ReadJson.read(path);
        return getJsonKeyMap(null,jsonObject);
    }

    public static Map<String,JsonDataType> getJsonKeyMap(String head, JSONObject jsonObject){
        Map<String,JsonDataType> keyMap=new HashMap<>();
        Set<Map.Entry<String, Object>> entrySet = jsonObject.entrySet();
        Iterator<Map.Entry<String, Object>> iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> next = iterator.next();
            String key = next.getKey();
            Object value = next.getValue();
            JsonDataType jsonDataType;
            if (value instanceof String){
                jsonDataType = JsonDataType.String;
                String v= (String) value;
                if (v==null||v.length()==0){
                    jsonDataType=JsonDataType.NULL;
                }
            }else if (value instanceof Number){
                jsonDataType = JsonDataType.Number;
            }else if (value instanceof Boolean){
                jsonDataType = JsonDataType.Boolean;
            }else if (value instanceof JSONObject){
                jsonDataType = JsonDataType.JSONObject;
                Map<String,JsonDataType> map=getJsonKeyMap(head==null?key+":":head+key+":", (JSONObject) value);
                keyMap.putAll(map);
            }else if (value instanceof JSONArray){
                JSONArray jsonArray= (JSONArray) value;
                jsonDataType = JsonDataType.NULL;
                if (jsonArray.size()>0){
                    jsonDataType = JsonDataType.JSONArray;
                    Object o = jsonArray.get(0);
                    if (o instanceof JSONObject){
                        Map<String,JsonDataType> map=getJsonKeyMap(head==null?key+":":head+key+":", (JSONObject) o);
                        keyMap.putAll(map);
                    }
                }
            }else if (value==null){
                jsonDataType = JsonDataType.NULL;
            }else {
                jsonDataType = JsonDataType.UnknownType;
            }
            keyMap.put(head==null?key:head+key, jsonDataType);
        }
        return keyMap;
    }
}
