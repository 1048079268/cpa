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
        Map<String, JsonDataType> temp = new HashMap<>(template);
        StringBuffer notMatch=new StringBuffer();
        Set<String> nullValueKeys=new HashSet<>();
        //1.遍历待比对的json，如果有值为NULL的将键写入nullValueKeys
        dataMap.forEach((key, value) -> {
            if (value.equals(JsonDataType.NULL)) {
                nullValueKeys.add(key);
            }
        });
        //2.遍历比对模板，如果其键在nullValueKeys中，则删除其下相关的键值
        template.forEach((key, value) -> {
            boolean match = nullValueKeys.stream().anyMatch(nullValueKey -> key.matches(nullValueKey + ":.*"));
            if (match) {
                temp.remove(key);
            }
        });
        //3.对比模板，如果有新增或者变更则将信息写入notMatch，并在模板JSON中删除该键值
        dataMap.forEach((key, dataType) -> {
            JsonDataType tempType = temp.get(key);
            if (tempType == null) {
                notMatch.append("\n\r新增[ ").append(key).append(" ],类型为").append(dataType).append(";");
            } else {
                temp.remove(key);
                //如果模板为NULL或者待对比JSON为NULL则不进行类型变更比对
                if (!dataType.equals(JsonDataType.NULL) && !tempType.equals(JsonDataType.NULL)) {
                    if (!dataType.equals(tempType)) {
                        notMatch.append("\n\r变更[ ").append(key).append(" ]:").append(tempType).append("->").append(dataType).append(";");
                    }
                }
            }

        });
        //4.如果对比完成后模板JSON中还有剩下的键值，如果不为NULL，则这些键值是待对比JSON中没有的，即移除的
        temp.forEach((key, value) -> {
            if (!value.equals(JsonDataType.NULL)) {
                notMatch.append("\n\r移除[ ").append(key).append(" ],类型为").append(value).append(";");
            }
        });
        //5.如果notMatch不为空，则抛出结构改变异常，交给具体业务处理
        if (notMatch.length()>0){
            throw new StructureChangeException(notMatch.toString());
        }
    }
}
