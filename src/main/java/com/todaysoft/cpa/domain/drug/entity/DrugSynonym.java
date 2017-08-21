package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/9 9:56
 */
@Entity
@Table(name = "kt_drug_synonym", schema = "project_kb")
public class DrugSynonym implements Serializable {
    private static final long serialVersionUID = 1L;
    private String synonymKey;
    private String drugKey;
    private int drugId;
    private String drugSynonym;

    @Id
    @Column(name = "synonym_key", nullable = false, length = 64)
    public String getSynonymKey() {
        return synonymKey;
    }

    public void setSynonymKey(String synonymKey) {
        this.synonymKey = synonymKey;
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
    @Column(name = "drug_synonym", nullable = false, length = 500)
    public String getDrugSynonym() {
        return drugSynonym;
    }

    public void setDrugSynonym(String drugSynonym) {
        this.drugSynonym = drugSynonym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugSynonym that = (DrugSynonym) o;

        if (drugId != that.drugId) return false;
        if (synonymKey != null ? !synonymKey.equals(that.synonymKey) : that.synonymKey != null) return false;
        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (drugSynonym != null ? !drugSynonym.equals(that.drugSynonym) : that.drugSynonym != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = synonymKey != null ? synonymKey.hashCode() : 0;
        result = 31 * result + (drugKey != null ? drugKey.hashCode() : 0);
        result = 31 * result + drugId;
        result = 31 * result + (drugSynonym != null ? drugSynonym.hashCode() : 0);
        return result;
    }
}
