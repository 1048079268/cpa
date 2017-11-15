package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_kegg_pathway")
@IdClass(DrugKeggPathwayPK.class)
public class DrugKeggPathway implements Serializable {
    private static final long serialVersionUID = 1L;
    private String drugKey;
    private String pathwayKey;
    private int drugId;
    @JSONField(name = "id")
    private String keggId;
    @JSONField(name = "name")
    private String pathwayName;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "pathway_key", nullable = false, length = 64)
    public String getPathwayKey() {
        return pathwayKey;
    }

    public void setPathwayKey(String pathwayKey) {
        this.pathwayKey = pathwayKey;
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
    @Column(name = "kegg_id", nullable = false, length = 64)
    public String getKeggId() {
        return keggId;
    }

    public void setKeggId(String keggId) {
        this.keggId = keggId;
    }

    @Basic
    @Column(name = "pathway_name", nullable = false, length = 200)
    public String getPathwayName() {
        return pathwayName;
    }

    public void setPathwayName(String pathwayName) {
        this.pathwayName = pathwayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugKeggPathway that = (DrugKeggPathway) o;

        if (drugId != that.drugId) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (pathwayKey != null ? !pathwayKey.equals(that.pathwayKey) : that.pathwayKey != null) return false;
        if (keggId != null ? !keggId.equals(that.keggId) : that.keggId != null) return false;
        if (pathwayName != null ? !pathwayName.equals(that.pathwayName) : that.pathwayName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (pathwayKey != null ? pathwayKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (keggId != null ? keggId.hashCode() : 0);
        result = 31 * result + (pathwayName != null ? pathwayName.hashCode() : 0);
        return result;
    }
}
