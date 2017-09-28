package com.todaysoft.cpa.domain.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
public class ClinicalTrialGenePK implements Serializable {
    private String clinicalTrialKey;
    private String geneKey;

    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    @Id
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Column(name = "gene_key", nullable = false, length = 64)
    @Id
    public String getGeneKey() {
        return geneKey;
    }

    public void setGeneKey(String geneKey) {
        this.geneKey = geneKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalTrialGenePK that = (ClinicalTrialGenePK) o;

        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(that.geneKey) : that.geneKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        return result;
    }
}
