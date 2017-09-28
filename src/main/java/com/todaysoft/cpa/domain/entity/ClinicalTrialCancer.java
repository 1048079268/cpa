package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
@Entity
@Table(name = "kt_clinical_trial_cancer")
@IdClass(ClinicalTrialCancerPK.class)
public class ClinicalTrialCancer {
    private String clinicalTrialKey;
    private String cancerKey;
    private Integer doid;
    private String clinicalTrailId;

    @Id
    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Id
    @Column(name = "cancer_key", nullable = false, length = 64)
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Basic
    @Column(name = "doid", nullable = false)
    public Integer getDoid() {
        return doid;
    }

    public void setDoid(Integer doid) {
        this.doid = doid;
    }

    @Basic
    @Column(name = "clinical_trial_id", nullable = false, length = 64)
    public String getClinicalTrailId() {
        return clinicalTrailId;
    }

    public void setClinicalTrailId(String clinicalTrailId) {
        this.clinicalTrailId = clinicalTrailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClinicalTrialCancer)) return false;

        ClinicalTrialCancer that = (ClinicalTrialCancer) o;

        if (getClinicalTrialKey() != null ? !getClinicalTrialKey().equals(that.getClinicalTrialKey()) : that.getClinicalTrialKey() != null)
            return false;
        if (getCancerKey() != null ? !getCancerKey().equals(that.getCancerKey()) : that.getCancerKey() != null)
            return false;
        if (getDoid() != null ? !getDoid().equals(that.getDoid()) : that.getDoid() != null) return false;
        return getClinicalTrailId() != null ? getClinicalTrailId().equals(that.getClinicalTrailId()) : that.getClinicalTrailId() == null;
    }

    @Override
    public int hashCode() {
        int result = getClinicalTrialKey() != null ? getClinicalTrialKey().hashCode() : 0;
        result = 31 * result + (getCancerKey() != null ? getCancerKey().hashCode() : 0);
        result = 31 * result + (getDoid() != null ? getDoid().hashCode() : 0);
        result = 31 * result + (getClinicalTrailId() != null ? getClinicalTrailId().hashCode() : 0);
        return result;
    }
}
