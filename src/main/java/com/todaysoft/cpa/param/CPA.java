package com.todaysoft.cpa.param;

import com.todaysoft.cpa.compare.JsonDataType;

import java.util.*;

/**
 * @desc: 每个接口各自的信息
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:25
 */
public enum CPA {
    GENE,DRUG,VARIANT,EVIDENCE,PROTEIN,CLINICAL_TRIAL,REGIMEN,MUTATION_STATISTICS;
    public Set<String> dbId;
    public String contentUrl;
    public String name;
    public Map<String,JsonDataType> tempStructureMap;
    CPA(){
        dbId= Collections.synchronizedSet(new HashSet<>());
        contentUrl=null;
        name=null;
        tempStructureMap=new HashMap<>();
    }
}
