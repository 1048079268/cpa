package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

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
    private Boolean isPrimary;
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

    @Basic
    @Column(name = "is_primary", nullable = true)
    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DrugExternalId)) return false;
        DrugExternalId that = (DrugExternalId) o;
        return getDrugId() == that.getDrugId() &&
                Objects.equals(getDrugKey(), that.getDrugKey()) &&
                Objects.equals(getExternalIdSource(), that.getExternalIdSource()) &&
                Objects.equals(getExternalId(), that.getExternalId()) &&
                Objects.equals(isPrimary, that.isPrimary);
    }

    @Override
    public int hashCode() {

        return Objects.hash(getDrugKey(), getDrugId(), getExternalIdSource(), getExternalId(), isPrimary);
    }
}
