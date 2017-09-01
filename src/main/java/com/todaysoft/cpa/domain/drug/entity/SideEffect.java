package com.todaysoft.cpa.domain.drug.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:35
 */
@Entity
@Table(name = "kt_side_effect")
public class SideEffect {
    private String sideEffectKey;
    private String umlsConceptId;
    private String meddraId;
    private String kindOfTerm;
    private String sideEffectName;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;

    @Id
    @Column(name = "side_effect_key", nullable = false, length = 64)
    public String getSideEffectKey() {
        return sideEffectKey;
    }

    public void setSideEffectKey(String sideEffectKey) {
        this.sideEffectKey = sideEffectKey;
    }

    @Basic
    @Column(name = "umls_concept_id", nullable = true, length = 64)
    public String getUmlsConceptId() {
        return umlsConceptId;
    }

    public void setUmlsConceptId(String umlsConceptId) {
        this.umlsConceptId = umlsConceptId;
    }

    @Basic
    @Column(name = "meddra_id", nullable = true, length = 64)
    public String getMeddraId() {
        return meddraId;
    }

    public void setMeddraId(String meddraId) {
        this.meddraId = meddraId;
    }

    @Basic
    @Column(name = "kind_of_term", nullable = true, length = 64)
    public String getKindOfTerm() {
        return kindOfTerm;
    }

    public void setKindOfTerm(String kindOfTerm) {
        this.kindOfTerm = kindOfTerm;
    }

    @Basic
    @Column(name = "side_effect_name", nullable = true, length = 200)
    public String getSideEffectName() {
        return sideEffectName;
    }

    public void setSideEffectName(String sideEffectName) {
        this.sideEffectName = sideEffectName;
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

        SideEffect that = (SideEffect) o;

        if (sideEffectKey != null ? !sideEffectKey.equals(that.sideEffectKey) : that.sideEffectKey != null)
            return false;
        if (umlsConceptId != null ? !umlsConceptId.equals(that.umlsConceptId) : that.umlsConceptId != null)
            return false;
        if (meddraId != null ? !meddraId.equals(that.meddraId) : that.meddraId != null) return false;
        if (kindOfTerm != null ? !kindOfTerm.equals(that.kindOfTerm) : that.kindOfTerm != null) return false;
        if (sideEffectName != null ? !sideEffectName.equals(that.sideEffectName) : that.sideEffectName != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(that.createdWay) : that.createdWay != null) return false;
        if (checkState != null ? !checkState.equals(that.checkState) : that.checkState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sideEffectKey != null ? sideEffectKey.hashCode() : 0;
        result = 31 * result + (umlsConceptId != null ? umlsConceptId.hashCode() : 0);
        result = 31 * result + (meddraId != null ? meddraId.hashCode() : 0);
        result = 31 * result + (kindOfTerm != null ? kindOfTerm.hashCode() : 0);
        result = 31 * result + (sideEffectName != null ? sideEffectName.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
