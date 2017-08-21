package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
public class DrugKeggPathwayPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private String drugKey;
    private String pathwayKey;

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "pathway_key", nullable = false, length = 64)
    @Id
    public String getPathwayKey() {
        return pathwayKey;
    }

    public void setPathwayKey(String pathwayKey) {
        this.pathwayKey = pathwayKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugKeggPathwayPK that = (DrugKeggPathwayPK) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (pathwayKey != null ? !pathwayKey.equals(that.pathwayKey) : that.pathwayKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (pathwayKey != null ? pathwayKey.hashCode() : 0);
        return result;
    }
}
