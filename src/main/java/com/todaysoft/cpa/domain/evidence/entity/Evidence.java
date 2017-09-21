package com.todaysoft.cpa.domain.evidence.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/14 15:40
 */
@Entity
@Table(name = "kt_evidence")
public class Evidence {
    private String evidenceKey;
    @JSONField(name = "id")
    private Integer evidenceId;
    private String cancerKey;
    @JSONField(name = "unParse")
    private Integer doid;
    private String doidName;
    private String variantKey;
    @JSONField(name = "variantId")
    private Integer variantId;
    @JSONField(name = "evidenceType")
    private String evidenceType;
    @JSONField(name = "evidenceLevel")
    private Integer evidenceLevel;
    @JSONField(name = "evidenceDirection")
    private String evidenceDirection;
    @JSONField(name = "summary")
    private String summary;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

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
    @Column(name = "cancer_key", nullable = true, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Basic
    @Column(name = "doid", nullable = true)
    public Integer getDoid() {
        return doid;
    }

    public void setDoid(Integer doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "doid_name", nullable = true, length = 200)
    public String getDoidName() {
        return doidName;
    }

    public void setDoidName(String doidName) {
        this.doidName = doidName;
    }

    @Basic
    @Column(name = "variant_key", nullable = true, length = 64)
    public String getVariantKey() {
        return variantKey;
    }

    public void setVariantKey(String variantKey) {
        this.variantKey = variantKey;
    }

    @Basic
    @Column(name = "variant_id", nullable = true)
    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(Integer variantId) {
        this.variantId = variantId;
    }

    @Basic
    @Column(name = "evidence_type", nullable = true, length = 200)
    public String getEvidenceType() {
        return evidenceType;
    }

    public void setEvidenceType(String evidenceType) {
        this.evidenceType = evidenceType;
    }

    @Basic
    @Column(name = "evidence_level", nullable = true)
    public Integer getEvidenceLevel() {
        return evidenceLevel;
    }

    public void setEvidenceLevel(Integer evidenceLevel) {
        this.evidenceLevel = evidenceLevel;
    }

    @Basic
    @Column(name = "evidence_direction", nullable = true, length = 32)
    public String getEvidenceDirection() {
        return evidenceDirection;
    }

    public void setEvidenceDirection(String evidenceDirection) {
        this.evidenceDirection = evidenceDirection;
    }

    @Basic
    @Column(name = "summary", nullable = true, length = -1)
    @Type(type = "text")
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Basic
    @Column(name = "created_at", nullable = true)
    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "created_way", nullable = true)
    public Integer getCreatedWay() {
        return createdWay;
    }

    public void setCreatedWay(Integer createdWay) {
        this.createdWay = createdWay;
    }

    @Basic
    @Column(name = "check_state", nullable = true)
    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evidence evidence = (Evidence) o;

        if (evidenceKey != null ? !evidenceKey.equals(evidence.evidenceKey) : evidence.evidenceKey != null)
            return false;
        if (evidenceId != null ? !evidenceId.equals(evidence.evidenceId) : evidence.evidenceId != null) return false;
        if (cancerKey != null ? !cancerKey.equals(evidence.cancerKey) : evidence.cancerKey != null) return false;
        if (doid != null ? !doid.equals(evidence.doid) : evidence.doid != null) return false;
        if (doidName != null ? !doidName.equals(evidence.doidName) : evidence.doidName != null) return false;
        if (variantKey != null ? !variantKey.equals(evidence.variantKey) : evidence.variantKey != null) return false;
        if (variantId != null ? !variantId.equals(evidence.variantId) : evidence.variantId != null) return false;
        if (evidenceType != null ? !evidenceType.equals(evidence.evidenceType) : evidence.evidenceType != null)
            return false;
        if (evidenceLevel != null ? !evidenceLevel.equals(evidence.evidenceLevel) : evidence.evidenceLevel != null)
            return false;
        if (evidenceDirection != null ? !evidenceDirection.equals(evidence.evidenceDirection) : evidence.evidenceDirection != null)
            return false;
        if (summary != null ? !summary.equals(evidence.summary) : evidence.summary != null) return false;
        if (createdAt != null ? !createdAt.equals(evidence.createdAt) : evidence.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(evidence.createdWay) : evidence.createdWay != null) return false;
        if (checkState != null ? !checkState.equals(evidence.checkState) : evidence.checkState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = evidenceKey != null ? evidenceKey.hashCode() : 0;
        result = 31 * result + (evidenceId != null ? evidenceId.hashCode() : 0);
        result = 31 * result + (cancerKey != null ? cancerKey.hashCode() : 0);
        result = 31 * result + (doid != null ? doid.hashCode() : 0);
        result = 31 * result + (doidName != null ? doidName.hashCode() : 0);
        result = 31 * result + (variantKey != null ? variantKey.hashCode() : 0);
        result = 31 * result + (variantId != null ? variantId.hashCode() : 0);
        result = 31 * result + (evidenceType != null ? evidenceType.hashCode() : 0);
        result = 31 * result + (evidenceLevel != null ? evidenceLevel.hashCode() : 0);
        result = 31 * result + (evidenceDirection != null ? evidenceDirection.hashCode() : 0);
        result = 31 * result + (summary != null ? summary.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
