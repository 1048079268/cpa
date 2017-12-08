package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/12/8 14:19
 */
public class VariantMutationStatistic {
    private String typeKey;
    @JSONField(name = "doid")
    private String doid;
    @JSONField(name = "mutationId")
    private String cosmicId;
    @JSONField(name = "numOfSamples")
    private Integer numOfSamples;

    public VariantMutationStatistic(String typeKey, Integer doid, String cosmicId) {
        this.typeKey = typeKey;
        this.doid = String.valueOf(doid);
        this.cosmicId = cosmicId;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getDoid() {
        return doid;
    }

    public void setDoid(String doid) {
        this.doid = doid;
    }

    public String getCosmicId() {
        return cosmicId;
    }

    public void setCosmicId(String cosmicId) {
        this.cosmicId = cosmicId;
    }

    public Integer getNumOfSamples() {
        return numOfSamples;
    }

    public void setNumOfSamples(Integer numOfSamples) {
        this.numOfSamples = numOfSamples;
    }
}
