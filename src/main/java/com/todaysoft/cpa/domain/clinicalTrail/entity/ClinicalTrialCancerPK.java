package com.todaysoft.cpa.domain.clinicalTrail.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
public class ClinicalTrialCancerPK implements Serializable {
    private String clinicalTrialKey;
    private String cancerKey;

    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    @Id
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Column(name = "cancer_key", nullable = false, length = 64)
    @Id
    public String getCancerKey() {
        return cancerKey;
    }

    public void setCancerKey(String cancerKey) {
        this.cancerKey = cancerKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalTrialCancerPK that = (ClinicalTrialCancerPK) o;

        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (cancerKey != null ? !cancerKey.equals(that.cancerKey) : that.cancerKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0;
        result = 31 * result + (cancerKey != null ? cancerKey.hashCode() : 0);
        return result;
    }
}
