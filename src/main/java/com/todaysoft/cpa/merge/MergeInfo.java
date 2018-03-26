package com.todaysoft.cpa.merge;

import java.util.*;

/**
 * @desc: 合并信息记录
 * @author: 鱼唇的人类
 * @date: 2017/11/15 10:15
 */
public enum  MergeInfo {
    DRUG,DRUG_PRODUCT,KEGG_PATHWAY,CLINICAL_TRIAL,GENE;
    //返回的合并列表
    public Set<List<String>> mergeList;
    //待审核的列表
    public List<List<String>> checkList;
    //标识该项重合的信息集合，防止重复写入待审核列表，当处理返回的数据成功后删除。
    public Set<String> sign;
    //是否需要人工审核
    public boolean isNeedArtificialCheck;
    MergeInfo(){
        mergeList=new HashSet<>();
        checkList=new ArrayList<>();
        isNeedArtificialCheck=false;//TODO 暂时不需要人工审核
        sign= Collections.synchronizedSet(new HashSet<>());
    }
}
