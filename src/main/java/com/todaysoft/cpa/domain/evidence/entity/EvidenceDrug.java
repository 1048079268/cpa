package com.todaysoft.cpa.domain.evidence.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:40
 */
@Entity
@Table(name = "kt_evidence_drug", schema = "project_kb_en", catalog = "")
@IdClass(EvidenceDrugPK.class)
public class EvidenceDrug {
    private String drugKey;
    private String evidenceKey;
    private Integer evidenceId;
    private Integer drugId;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "evidence_key", nullable = false, length = 64)
    public String getEvidenceKey() {
        return evidenceKey;
    }

    public void setEvidenceKey(String evidenceKey) {
        this.evidenceKey = evidenceKey;
    }

    @Basic
    @Column(name = "evidence_id", nullable = false)
    public Integer getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(Integer evidenceId) {
        this.evidenceId = evidenceId;
    }

    @Basic
    @Column(name = "drug_id", nullable = false)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvidenceDrug that = (EvidenceDrug) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (evidenceKey != null ? !evidenceKey.equals(that.evidenceKey) : that.evidenceKey != null) return false;
        if (evidenceId != null ? !evidenceId.equals(that.evidenceId) : that.evidenceId != null) return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (evidenceKey != null ? evidenceKey.hashCode() : 0);
        result = 31 * result + (evidenceId != null ? evidenceId.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        return result;
    }
}
