package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/6 13:51
 */
public class DrugClinicalTrialPK implements Serializable {
    private String drugKey;
    private String clinicalTrialKey;

    @Column(name = "drug_key", nullable = false, length = 64)
    @Id
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    @Id
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugClinicalTrialPK that = (DrugClinicalTrialPK) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0);
        return result;
    }
}
