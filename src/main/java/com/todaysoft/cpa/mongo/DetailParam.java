package com.todaysoft.cpa.mongo;

import com.alibaba.fastjson.JSONObject;
import com.todaysoft.cpa.mongo.domain.MongoDetail;
import com.todaysoft.cpa.param.CPA;

/**
 * 数据
 * @author liceyo
 * @version 2018/7/17
 */
public class DetailParam extends MongoDetail {
    /**
     * 是否保存成功
     */
    private boolean saveSuccess=false;
    private JSONObject cnData;
    private JSONObject enData;
    private CPA cpa;

    public DetailParam(String dataId) {
        super.setDataId(dataId);
    }

    public DetailParam(MongoDetail d) {
        super.setDataId(d.getDataId());
        super.setErrorInfo(d.getErrorInfo());
        super.setId(d.getId());
        super.setModuleName(d.getModuleName());
        super.setUpdateSince(d.getUpdateSince());
    }

    public MongoDetail toMongoDetail(){
        MongoDetail mongoDetail=new MongoDetail();
        mongoDetail.setId(this.getId());
        mongoDetail.setDataId(this.getDataId());
        mongoDetail.setErrorInfo(this.getErrorInfo());
        mongoDetail.setModuleName(this.getModuleName());
        mongoDetail.setUpdateSince(this.getUpdateSince());
        return mongoDetail;
    }


    public boolean isSaveSuccess() {
        return saveSuccess;
    }

    public void setSaveSuccess(boolean saveSuccess) {
        this.saveSuccess = saveSuccess;
    }

    public JSONObject getCnData() {
        return cnData;
    }

    public void setCnData(JSONObject cnData) {
        this.cnData = cnData;
    }

    public JSONObject getEnData() {
        return enData;
    }

    public void setEnData(JSONObject enData) {
        this.enData = enData;
    }

    public CPA getCpa() {
        return cpa;
    }

    public void setCpa(CPA cpa) {
        this.cpa = cpa;
    }
}
