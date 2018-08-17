package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.Objects;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:40
 */
@Entity
@Table(name = "kt_evidence_reference")
public class EvidenceReference {
    private String evidenceReferenceKey;
    private String evidenceKey;
    private Integer evidenceId;
    @JSONField(name = "refType")
    private String refType;
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
        if (!(o instanceof EvidenceReference)) return false;
        EvidenceReference that = (EvidenceReference) o;
        return Objects.equals(getEvidenceKey(), that.getEvidenceKey()) &&
                Objects.equals(getEvidenceId(), that.getEvidenceId()) &&
                Objects.equals(getRefType(), that.getRefType()) &&
                Objects.equals(getExternalId(), that.getExternalId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getEvidenceKey(), getEvidenceId(), getRefType(), getExternalId());
    }
}
