package com.todaysoft.cpa.domain.evidence.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:40
 */
@Entity
@Table(name = "kt_evidence_reference", schema = "project_kb_en", catalog = "")
public class EvidenceReference {
    private String evidenceReferenceKey;
    private String evidenceKey;
    private Integer evidenceId;
    @JSONField(name = "refType")
    private String refType;
    private Integer referenceId;
    @JSONField(name = "externalId")
    private Integer externalId;

    @Id
    @Column(name = "evidence_reference_key", nullable = false, length = 64)
    public String getEvidenceReferenceKey() {
        return evidenceReferenceKey;
    }

    public void setEvidenceReferenceKey(String evidenceReferenceKey) {
        this.evidenceReferenceKey = evidenceReferenceKey;
    }

    @Basic
    @Column(name = "evidence_key", nullable = true, length = 64)
    public String getEvidenceKey() {
        return evidenceKey;
    }

    public void setEvidenceKey(String evidenceKey) {
        this.evidenceKey = evidenceKey;
    }

    @Basic
    @Column(name = "evidence_id", nullable = true)
    public Integer getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(Integer evidenceId) {
        this.evidenceId = evidenceId;
    }

    @Basic
    @Column(name = "ref_type", nullable = true, length = 32)
    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    @Basic
    @Column(name = "reference_id", nullable = true)
    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    @Basic
    @Column(name = "external_id", nullable = true)
    public Integer getExternalId() {
        return externalId;
    }

    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EvidenceReference that = (EvidenceReference) o;

        if (evidenceReferenceKey != null ? !evidenceReferenceKey.equals(that.evidenceReferenceKey) : that.evidenceReferenceKey != null)
            return false;
        if (evidenceKey != null ? !evidenceKey.equals(that.evidenceKey) : that.evidenceKey != null) return false;
        if (evidenceId != null ? !evidenceId.equals(that.evidenceId) : that.evidenceId != null) return false;
        if (refType != null ? !refType.equals(that.refType) : that.refType != null) return false;
        if (referenceId != null ? !referenceId.equals(that.referenceId) : that.referenceId != null) return false;
        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evidenceReferenceKey != null ? evidenceReferenceKey.hashCode() : 0;
        result = 31 * result + (evidenceKey != null ? evidenceKey.hashCode() : 0);
        result = 31 * result + (evidenceId != null ? evidenceId.hashCode() : 0);
        result = 31 * result + (refType != null ? refType.hashCode() : 0);
        result = 31 * result + (referenceId != null ? referenceId.hashCode() : 0);
        result = 31 * result + (externalId != null ? externalId.hashCode() : 0);
        return result;
    }
}
