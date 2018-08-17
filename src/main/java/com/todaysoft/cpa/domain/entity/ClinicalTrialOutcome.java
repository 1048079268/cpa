package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
@Entity
@Table(name = "kt_clinical_trial_outcome")
public class ClinicalTrialOutcome {
    private String clinicalTrailOutcomeKey;
    private String clinicalTrialKey;
    private String clinicalTrailId;
    @JSONField(name = "category")
    private String category;
    @JSONField(name = "classification")
    private String classification;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "resultGroup")
    private String resultGroup;
    @JSONField(name = "units")
    private String unit;
    @JSONField(name = "paramType")
    private String paramType;
    @JSONField(name = "paramValue")
    private Double paramValue;
    @JSONField(name = "dispType")
    private String dispType;
    @JSONField(name = "dispValue")
    private Double dispValue;
    @JSONField(name = "dispMin")
    private Double dispMin;
    @JSONField(name = "dispMax")
    private Double dispMax;

    @Id
    @Column(name = "clinical_trial_outcome_key", nullable = false, length = 64)
    public String getClinicalTrailOutcomeKey() {
        return clinicalTrailOutcomeKey;
    }

    public void setClinicalTrailOutcomeKey(String clinicalTrailOutcomeKey) {
        this.clinicalTrailOutcomeKey = clinicalTrailOutcomeKey;
    }

    @Basic
    @Column(name = "clinical_trial_key", nullable = true, length = 64)
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Basic
    @Column(name = "clinical_trial_id", nullable = false, length = 64)
    public String getClinicalTrailId() {
        return clinicalTrailId;
    }

    public void setClinicalTrailId(String clinicalTrailId) {
        this.clinicalTrailId = clinicalTrailId;
    }

    @Basic
    @Column(name = "category", nullable = true, length = 200)
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Basic
    @Column(name = "classification", nullable = true, length = 200)
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Basic
    @Column(name = "title", nullable = true, length = -1)
    @Type(type = "text")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "result_group", nullable = true, length = 200)
    public String getResultGroup() {
        return resultGroup;
    }

    public void setResultGroup(String resultGroup) {
        this.resultGroup = resultGroup;
    }

    @Basic
    @Column(name = "unit", nullable = true, length = 200)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Basic
    @Column(name = "param_type", nullable = true, length = 200)
    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    @Basic
    @Column(name = "param_value", nullable = true, precision = 0)
    public Double getParamValue() {
        return paramValue;
    }

    public void setParamValue(Double paramValue) {
        this.paramValue = paramValue;
    }

    @Basic
    @Column(name = "disp_type", nullable = true, length = 200)
    public String getDispType() {
        return dispType;
    }

    public void setDispType(String dispType) {
        this.dispType = dispType;
    }

    @Basic
    @Column(name = "disp_value", nullable = true, precision = 0)
    public Double getDispValue() {
        return dispValue;
    }

    public void setDispValue(Double dispValue) {
        this.dispValue = dispValue;
    }

    @Basic
    @Column(name = "disp_min", nullable = true, precision = 0)
    public Double getDispMin() {
        return dispMin;
    }

    public void setDispMin(Double dispMin) {
        this.dispMin = dispMin;
    }

    @Basic
    @Column(name = "disp_max", nullable = true, precision = 0)
    public Double getDispMax() {
        return dispMax;
    }

    public void setDispMax(Double dispMax) {
        this.dispMax = dispMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClinicalTrialOutcome)) return false;
        ClinicalTrialOutcome that = (ClinicalTrialOutcome) o;
        return Objects.equals(getClinicalTrialKey(), that.getClinicalTrialKey()) &&
                Objects.equals(getClinicalTrailId(), that.getClinicalTrailId()) &&
                Objects.equals(getCategory(), that.getCategory()) &&
                Objects.equals(getClassification(), that.getClassification()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getResultGroup(), that.getResultGroup()) &&
                Objects.equals(getUnit(), that.getUnit()) &&
                Objects.equals(getParamType(), that.getParamType()) &&
                Objects.equals(getParamValue(), that.getParamValue()) &&
                Objects.equals(getDispType(), that.getDispType()) &&
                Objects.equals(getDispValue(), that.getDispValue()) &&
                Objects.equals(getDispMin(), that.getDispMin()) &&
                Objects.equals(getDispMax(), that.getDispMax());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClinicalTrialKey(), getClinicalTrailId(), getCategory(), getClassification(), getTitle(), getResultGroup(), getUnit(), getParamType(), getParamValue(), getDispType(), getDispValue(), getDispMin(), getDispMax());
    }
}
