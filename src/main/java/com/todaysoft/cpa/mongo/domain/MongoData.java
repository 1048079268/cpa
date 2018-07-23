package com.todaysoft.cpa.mongo.domain;

import org.springframework.data.annotation.Id;

/**
 * @author liceyo
 * @version 2018/7/20
 */
public class MongoData {
    @Id
    private String id;

    private String updateSince;

    private Boolean ktMysqlSyncStatus;

    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdateSince() {
        return updateSince;
    }

    public void setUpdateSince(String updateSince) {
        this.updateSince = updateSince;
    }

    public Boolean getKtMysqlSyncStatus() {
        return ktMysqlSyncStatus;
    }

    public void setKtMysqlSyncStatus(Boolean ktMysqlSyncStatus) {
        this.ktMysqlSyncStatus = ktMysqlSyncStatus;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MongoData{" +
                "id='" + id + '\'' +
                ", updateSince='" + updateSince + '\'' +
                ", ktMysqlSyncStatus=" + ktMysqlSyncStatus +
                ", data=" + data +
                '}';
    }
}
