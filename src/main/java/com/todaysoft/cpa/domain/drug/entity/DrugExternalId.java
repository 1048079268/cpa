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
@Table(name = "kt_drug_external_id")
public class DrugExternalId implements Serializable {
    private static final long serialVersionUID = 1L;
    private String externalIdKey;
    private String drugKey;
    private int drugId;
    @JSONField(name = "externalSource")
    private String externalIdSource;
    @JSONField(name = "externalId")
    private String externalId;

    @Id
    @Column(name = "external_id_key", nullable = false, length = 64)
    public String getExternalIdKey() {
        return externalIdKey;
    }

    public void setExternalIdKey(String externalIdKey) {
        this.externalIdKey = externalIdKey;
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
    @Column(name = "drug_id", nullable = false)
    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "external_id_source", nullable = true, length = 64)
    public String getExternalIdSource() {
        return externalIdSource;
    }

    public void setExternalIdSource(String externalIdSource) {
        this.externalIdSource = externalIdSource;
    }

    @Basic
    @Column(name = "external_id", nullable = true, length = 64)
    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugExternalId that = (DrugExternalId) o;

        if (drugId != that.drugId) return false;
        if (externalIdKey != null ? !externalIdKey.equals(that.externalIdKey) : that.externalIdKey != null)
            return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (externalIdSource != null ? !externalIdSource.equals(that.externalIdSource) : that.externalIdSource != null)
            return false;
        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalIdKey != null ? externalIdKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (externalIdSource != null ? externalIdSource.hashCode() : 0);
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        return result;
    }
}
