package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.Type;

import javax.persistence.*;

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
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalTrialOutcome that = (ClinicalTrialOutcome) o;

        if (clinicalTrailOutcomeKey != null ? !clinicalTrailOutcomeKey.equals(that.clinicalTrailOutcomeKey) : that.clinicalTrailOutcomeKey != null)
            return false;
        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (clinicalTrailId != null ? !clinicalTrailId.equals(that.clinicalTrailId) : that.clinicalTrailId != null)
            return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (classification != null ? !classification.equals(that.classification) : that.classification != null)
            return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (resultGroup != null ? !resultGroup.equals(that.resultGroup) : that.resultGroup != null) return false;
        if (unit != null ? !unit.equals(that.unit) : that.unit != null) return false;
        if (paramType != null ? !paramType.equals(that.paramType) : that.paramType != null) return false;
        if (paramValue != null ? !paramValue.equals(that.paramValue) : that.paramValue != null) return false;
        if (dispType != null ? !dispType.equals(that.dispType) : that.dispType != null) return false;
        if (dispValue != null ? !dispValue.equals(that.dispValue) : that.dispValue != null) return false;
        if (dispMin != null ? !dispMin.equals(that.dispMin) : that.dispMin != null) return false;
        if (dispMax != null ? !dispMax.equals(that.dispMax) : that.dispMax != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalTrailOutcomeKey != null ? clinicalTrailOutcomeKey.hashCode() : 0;
        result = 31 * result + (clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0);
        result = 31 * result + (clinicalTrailId != null ? clinicalTrailId.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (classification != null ? classification.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (resultGroup != null ? resultGroup.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (paramType != null ? paramType.hashCode() : 0);
        result = 31 * result + (paramValue != null ? paramValue.hashCode() : 0);
        result = 31 * result + (dispType != null ? dispType.hashCode() : 0);
        result = 31 * result + (dispValue != null ? dispValue.hashCode() : 0);
        result = 31 * result + (dispMin != null ? dispMin.hashCode() : 0);
        result = 31 * result + (dispMax != null ? dispMax.hashCode() : 0);
        return result;
    }
}
