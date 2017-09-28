package com.todaysoft.cpa.domain.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/6 13:51
 */
@Entity
@Table(name = "kt_drug_clinical_trial")
@IdClass(DrugClinicalTrialPK.class)
public class DrugClinicalTrial {
    private String drugKey;
    private String clinicalTrialKey;
    private Integer drugId;
    private String clinicalTrialId;

    @Id
    @Column(name = "drug_key", nullable = false, length = 64)
    public String getDrugKey() {
        return drugKey;
    }

    public void setDrugKey(String drugKey) {
        this.drugKey = drugKey;
    }

    @Id
    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Basic
    @Column(name = "drug_id", nullable = false)
    public Integer getDrugId() {
        return drugId;
    }

    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    @Basic
    @Column(name = "clinical_trial_id", nullable = false, length = 64)
    public String getClinicalTrialId() {
        return clinicalTrialId;
    }

    public void setClinicalTrialId(String clinicalTrialId) {
        this.clinicalTrialId = clinicalTrialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrugClinicalTrial that = (DrugClinicalTrial) o;

        if (drugKey != null ? !drugKey.equals(that.drugKey) : that.drugKey != null) return false;
        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (drugId != null ? !drugId.equals(that.drugId) : that.drugId != null) return false;
        if (clinicalTrialId != null ? !clinicalTrialId.equals(that.clinicalTrialId) : that.clinicalTrialId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = drugKey != null ? drugKey.hashCode() : 0;
        result = 31 * result + (clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0);
        result = 31 * result + (drugId != null ? drugId.hashCode() : 0);
        result = 31 * result + (clinicalTrialId != null ? clinicalTrialId.hashCode() : 0);
        return result;
    }
}
