package com.todaysoft.cpa.mongo.domain;

import com.todaysoft.cpa.utils.PkGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author liceyo
 * @version 2018/7/19
 */
@Document(collection = "errorDetail")
public class MongoDetail {
    @Id
    private String id;
    /**
     * 数据id
     */
    private String dataId;

    /**
     * 模块信息
     */
    private String moduleName;

    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 更新时间戳
     */
    private String updateSince;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }


    public String getUpdateSince() {
        return updateSince;
    }

    public void setUpdateSince(String updateSince) {
        this.updateSince = updateSince;
    }
}
