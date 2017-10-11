package com.todaysoft.cpa.compare;

import com.todaysoft.cpa.utils.DataException;
import com.todaysoft.cpa.utils.StructureChangeException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/10/10 12:00
 */
public class CompareJsonStructure {
    public static void compare(Map<String,JsonDataType> template,Map<String,JsonDataType> dataMap) throws StructureChangeException {
        Map<String,JsonDataType> temp=new HashMap<>();
        temp.putAll(template);
        StringBuffer notMatch=new StringBuffer();
        Set<String> nullValueKeys=new HashSet<>();
        //1.遍历待比对的json，如果有值为NULL的将键写入nullValueKeys
        dataMap.entrySet().stream().forEach(data->{
            if (data.getValue().equals(JsonDataType.NULL)){
                nullValueKeys.add(data.getKey());
            }
        });
        //2.遍历比对模板，如果其键在nullValueKeys中，则删除其下相关的键值
        template.entrySet().stream().forEach(data->{
            boolean match = nullValueKeys.stream().anyMatch(key -> data.getKey().matches(key + ":.*"));
            if (match){
                temp.remove(data.getKey());
            }
        });
        //3.对比模板，如果有新增或者变更则将信息写入notMatch，并在模板JSON中删除该键值
        dataMap.entrySet().stream().forEach(data->{
            JsonDataType tempType = temp.get(data.getKey());
            JsonDataType dataType=data.getValue();
            if (tempType==null){
                notMatch.append("\n\r新增[ "+data.getKey()+" ],类型为"+data.getValue());
            }else {
                temp.remove(data.getKey());
                if (!dataType.equals(JsonDataType.NULL)){
                    if (!dataType.equals(tempType)){
                        notMatch.append("\n\r变更[ "+data.getKey()+" ]:"+tempType+"->"+dataType);
                    }
                }
            }

        });
        //4.如果对比完成后模板JSON中还有剩下的键值，则这些键值是待对比JSON中没有的，即移除的
        temp.entrySet().stream().forEach(data->{
            if (!data.getValue().equals(JsonDataType.NULL)){
                notMatch.append("\n\r移除[ "+data.getKey()+" ],类型为"+data.getValue());
            }
        });
        //5.如果notMatch不为空，则抛出结构改变异常，交给具体业务处理
        if (notMatch.length()>0){
            throw new StructureChangeException(notMatch.toString());
        }
    }
}
