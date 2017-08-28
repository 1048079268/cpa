package com.todaysoft.cpa.domain.drug.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:55
 */
@Entity
@Table(name = "kt_drug_category")
public class DrugCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String categoryKey;
    private String drugKey;
    private Integer drugId;
    @JSONField(name = "meshId")
    private String meshId;
    @JSONField(name = "categoryName")
    private String categoryName;

    @Id
    @Column(name = "category_key", nullable = false, length = 64)
    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    @Basic
    @Column(name = "drug_key", nullable = true, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Basic
    @Column(name = "drug_id", nullable = true)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "mesh_id", nullable = true, length = 64)
    public String getMeshId() {
        return meshId;
    }

    public void setMeshId(String meshId) {
        this.meshId = meshId;
    }

    @Basic
    @Column(name = "category_name", nullable = true, length = 200)
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugCategory that = (DrugCategory) o;

        if (categoryKey != null ? !categoryKey.equals(that.categoryKey) : that.categoryKey != null) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (meshId != null ? !meshId.equals(that.meshId) : that.meshId != null) return false;
        if (categoryName != null ? !categoryName.equals(that.categoryName) : that.categoryName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = categoryKey != null ? categoryKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (meshId != null ? meshId.hashCode() : 0);
        result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
        return result;
    }
}
