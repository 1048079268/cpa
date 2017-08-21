package com.todaysoft.cpa.param;

/**
 * @desc: 抓取id时的分页
 * @author: 鱼唇的人类
 * @date: 2017/8/15 11:01
 */
public class Page {
    private String url;
    private int limit;
    private int offset;
    private int originalLimit;
    private int originalOffset;

    public synchronized void offset(){
        this.offset+=this.limit;
    }

    public synchronized void init(){
        this.offset=this.originalOffset;
        this.limit=this.originalLimit;
    }

    public Page(String url, int limit, int offset) {
        this.url = url;
        this.limit = limit;
        this.offset = offset;
        this.originalLimit=limit;
        this.originalOffset=offset;
    }

    public Page(String url) {
        this.url = url;
        this.limit=20;
        this.offset=0;
        this.originalOffset=this.offset;
        this.originalLimit=this.limit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
