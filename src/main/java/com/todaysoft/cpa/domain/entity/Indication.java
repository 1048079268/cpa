package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/1 9:35
 */
@Entity
@Table(name = "kt_indication")
public class Indication {
    private String indicationKey;
    private String stitchCompoundId;
    private String umlsConceptId;
    private String methodOfDetection;
    private String conceptName;
    private String meddraConceptType;
    private String umlsConceptIdOfMeddra;
    private String meddraConceptName;
    private Long createdAt;
    private Integer createdWay;
    private Integer checkState;
    private String createdByName;

    @Basic
    @Column(name = "created_by_name", nullable = true, length = 20)
    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    @Id
    @Column(name = "indication_key", nullable = false, length = 64)
    public String getIndicationKey() {
        return indicationKey;
    }

    public void setIndicationKey(String indicationKey) {
        this.indicationKey = indicationKey;
    }

    @Basic
    @Column(name = "stitch_compound_id", nullable = true, length = 64)
    public String getStitchCompoundId() {
        return stitchCompoundId;
    }

    public void setStitchCompoundId(String stitchCompoundId) {
        this.stitchCompoundId = stitchCompoundId;
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
    @Column(name = "method_of_detection", nullable = true, length = 64)
    public String getMethodOfDetection() {
        return methodOfDetection;
    }

    public void setMethodOfDetection(String methodOfDetection) {
        this.methodOfDetection = methodOfDetection;
    }

    @Basic
    @Column(name = "concept_name", nullable = true, length = 200)
    public String getConceptName() {
        return conceptName;
    }

    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    @Basic
    @Column(name = "meddra_concept_type", nullable = true, length = 64)
    public String getMeddraConceptType() {
        return meddraConceptType;
    }

    public void setMeddraConceptType(String meddraConceptType) {
        this.meddraConceptType = meddraConceptType;
    }

    @Basic
    @Column(name = "umls_concept_id_of_meddra", nullable = true, length = 64)
    public String getUmlsConceptIdOfMeddra() {
        return umlsConceptIdOfMeddra;
    }

    public void setUmlsConceptIdOfMeddra(String umlsConceptIdOfMeddra) {
        this.umlsConceptIdOfMeddra = umlsConceptIdOfMeddra;
    }

    @Basic
    @Column(name = "meddra_concept_name", nullable = true, length = 200)
    public String getMeddraConceptName() {
        return meddraConceptName;
    }

    public void setMeddraConceptName(String meddraConceptName) {
        this.meddraConceptName = meddraConceptName;
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

        Indication that = (Indication) o;

        if (indicationKey != null ? !indicationKey.equals(that.indicationKey) : that.indicationKey != null)
            return false;
        if (stitchCompoundId != null ? !stitchCompoundId.equals(that.stitchCompoundId) : that.stitchCompoundId != null)
            return false;
        if (umlsConceptId != null ? !umlsConceptId.equals(that.umlsConceptId) : that.umlsConceptId != null)
            return false;
        if (methodOfDetection != null ? !methodOfDetection.equals(that.methodOfDetection) : that.methodOfDetection != null)
            return false;
        if (conceptName != null ? !conceptName.equals(that.conceptName) : that.conceptName != null) return false;
        if (meddraConceptType != null ? !meddraConceptType.equals(that.meddraConceptType) : that.meddraConceptType != null)
            return false;
        if (umlsConceptIdOfMeddra != null ? !umlsConceptIdOfMeddra.equals(that.umlsConceptIdOfMeddra) : that.umlsConceptIdOfMeddra != null)
            return false;
        if (meddraConceptName != null ? !meddraConceptName.equals(that.meddraConceptName) : that.meddraConceptName != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (createdWay != null ? !createdWay.equals(that.createdWay) : that.createdWay != null) return false;
        if (checkState != null ? !checkState.equals(that.checkState) : that.checkState != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = indicationKey != null ? indicationKey.hashCode() : 0;
        result = 31 * result + (stitchCompoundId != null ? stitchCompoundId.hashCode() : 0);
        result = 31 * result + (umlsConceptId != null ? umlsConceptId.hashCode() : 0);
        result = 31 * result + (methodOfDetection != null ? methodOfDetection.hashCode() : 0);
        result = 31 * result + (conceptName != null ? conceptName.hashCode() : 0);
        result = 31 * result + (meddraConceptType != null ? meddraConceptType.hashCode() : 0);
        result = 31 * result + (umlsConceptIdOfMeddra != null ? umlsConceptIdOfMeddra.hashCode() : 0);
        result = 31 * result + (meddraConceptName != null ? meddraConceptName.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (createdWay != null ? createdWay.hashCode() : 0);
        result = 31 * result + (checkState != null ? checkState.hashCode() : 0);
        return result;
    }
}
