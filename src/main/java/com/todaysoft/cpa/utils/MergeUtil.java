package com.todaysoft.cpa.utils;

import com.alibaba.druid.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lichy
 * @version 2018/4/10
 * @desc
 */
public class MergeUtil {
    /**
     * 将两个别名合并去重后返回
     * @param alias1
     * @param alias2
     * @param separator
     * @return
     */
    public static String mergeAlias(String alias1,String alias2,String separator){
        if (alias1==null||alias1.isEmpty()){
            return alias2;
        }
        if (alias2==null||alias2.isEmpty()){
            return alias1;
        }
        List<String> result=new ArrayList<>();
        result.addAll(Arrays.asList(alias1.split(separator)));
        result.addAll(Arrays.asList(alias2.split(separator)));
        return String.join(separator,result.stream().distinct().collect(Collectors.toList()));
    }

    public static String cover(String o1,String o2){
        if (o1!=null&&!o1.trim().isEmpty()){
            return o1;
        }
        return o2;
    }
}
