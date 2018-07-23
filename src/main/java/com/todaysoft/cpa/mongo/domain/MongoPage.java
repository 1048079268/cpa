package com.todaysoft.cpa.mongo.domain;

import com.todaysoft.cpa.utils.PkGenerator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/**
 * @author liceyo
 * @version 2018/7/19
 */
@Document(collection="errorPage")
public class MongoPage {
    @Id
    private String id;

    /**
     * 页数
     */
    private Integer page;

    /**
     * 一页条数
     */
    private Integer limit=20;

    /**
     * 模块名
     */
    private String moduleName;

    /**
     * 更新时间
     */
    private String updateSince;

    /**
     * 生成id
     */
    public void generateId(){
        this.id= PkGenerator.md5(moduleName+"-"+page+"-"+limit+"-"+updateSince);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getUpdateSince() {
        return updateSince;
    }

    public void setUpdateSince(String updateSince) {
        this.updateSince = updateSince;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
