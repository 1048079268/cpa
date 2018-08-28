package com.todaysoft.cpa.param;

import com.todaysoft.cpa.compare.JsonDataType;

import java.util.*;

/**
 * @desc: 每个接口各自的信息
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:25
 */
public enum CPA {
    GENE("gene"),
    DRUG("drug"),
    VARIANT("variant"),
    EVIDENCE("evidence"),
    PROTEIN("protein"),
    CLINICAL_TRIAL("clinicalTrial"),
    REGIMEN("regimen"),
    MUTATION_STATISTICS("mutationStatistics");
    public Set<String> dbId;
    public String contentUrl;
    public String name;
    public Map<String,JsonDataType> tempStructureMap;
    private String propField;
    CPA(String propField){
        this.propField=propField;
        dbId= Collections.synchronizedSet(new HashSet<>());
        contentUrl=null;
        name=null;
        tempStructureMap=new HashMap<>();
    }


    public String getPropField() {
        return propField;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, JsonDataType> getTempStructureMap() {
        return tempStructureMap;
    }

    public void setTempStructureMap(Map<String, JsonDataType> tempStructureMap) {
        this.tempStructureMap = tempStructureMap;
    }

    /**
     * 英文表名
     * @return
     */
    public String enDbName(){
        if (MUTATION_STATISTICS.equals(this)){
            return this.name;
        }
        return this.name+"_"+GlobalVar.getLangEn();
    }

    /**
     * 中文表名
     * @return
     */
    public String cnDbName(){
        return this.name+"_"+GlobalVar.getLangZh();
    }
}
