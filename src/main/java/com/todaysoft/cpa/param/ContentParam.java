package com.todaysoft.cpa.param;

import com.todaysoft.cpa.service.BaseService;

/**
 * @desc: 抓取内容时所需的参数
 * @author: 鱼唇的人类
 * @date: 2017/8/15 10:24
 */
public class ContentParam {
    private CPA cpa;
    private String id;
    private BaseService baseService;
    private boolean hasDependence=false;
    private String dependenceKey=null;

    public static ContentParam create(ContentParam contentParam) {
        ContentParam param=new ContentParam(contentParam.getCpa(), contentParam.getBaseService(), contentParam.isHasDependence(), contentParam.getDependenceKey());
        return param;
    }

    public ContentParam(CPA cpa, BaseService baseService) {
        this.cpa = cpa;
        this.baseService = baseService;
        this.id=null;
    }
    public ContentParam(CPA cpa){
        this.cpa=cpa;
        this.id=null;
    }

    public ContentParam(CPA cpa, BaseService baseService, boolean hasDependence, String dependenceKey) {
        this.cpa = cpa;
        this.baseService = baseService;
        this.hasDependence = hasDependence;
        this.dependenceKey = dependenceKey;
    }

    public BaseService getBaseService() {
        return baseService;
    }

    public void setBaseService(BaseService baseService) {
        this.baseService = baseService;
    }

    public CPA getCpa() {
        return cpa;
    }

    public void setCpa(CPA cpa) {
        this.cpa = cpa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isHasDependence() {
        return hasDependence;
    }

    public void setHasDependence(boolean hasDependence) {
        this.hasDependence = hasDependence;
    }

    public String getDependenceKey() {
        return dependenceKey;
    }

    public void setDependenceKey(String dependenceKey) {
        this.dependenceKey = dependenceKey;
    }
}
