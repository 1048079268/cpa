package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_other_name")
public class DrugOtherName implements Serializable {
    private static final long serialVersionUID = 1L;
    private String otherNameKey;
    private String drugKey;
    private int drugId;
    private String otherName;

    @Id
    @Column(name = "other_name_key", nullable = false, length = 64)
    public String getOtherNameKey() {
        return otherNameKey;
    }

    public void setOtherNameKey(String otherNameKey) {
        this.otherNameKey = otherNameKey;
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
    @Column(name = "other_name", nullable = false, length = 500)
    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugOtherName that = (DrugOtherName) o;

        if (drugId != that.drugId) return false;
        if (otherNameKey != null ? !otherNameKey.equals(that.otherNameKey) : that.otherNameKey != null) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (otherName != null ? !otherName.equals(that.otherName) : that.otherName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = otherNameKey != null ? otherNameKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (otherName != null ? otherName.hashCode() : 0);
        return result;
    }
}
