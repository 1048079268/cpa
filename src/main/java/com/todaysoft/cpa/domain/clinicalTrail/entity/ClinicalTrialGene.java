package com.todaysoft.cpa.domain.clinicalTrail.entity;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/4 13:55
 */
@Entity
@Table(name = "kt_clinical_trial_gene")
@IdClass(ClinicalTrialGenePK.class)
public class ClinicalTrialGene {
    private String clinicalTrialKey;
    private String geneKey;
    private int clinicalTrailId;
    private int geneId;

    @Id
    @Column(name = "clinical_trial_key", nullable = false, length = 64)
    public String getClinicalTrialKey() {
        return clinicalTrialKey;
    }

    public void setClinicalTrialKey(String clinicalTrialKey) {
        this.clinicalTrialKey = clinicalTrialKey;
    }

    @Id
    @Column(name = "gene_key", nullable = false, length = 64)
    public String getGeneKey() {
        return geneKey;
    }

    public void setGeneKey(String geneKey) {
        this.geneKey = geneKey;
    }

    @Basic
    @Column(name = "clinical_trail_id", nullable = false)
    public int getClinicalTrailId() {
        return clinicalTrailId;
    }

    public void setClinicalTrailId(int clinicalTrailId) {
        this.clinicalTrailId = clinicalTrailId;
    }

    @Basic
    @Column(name = "gene_id", nullable = false)
    public int getGeneId() {
        return geneId;
    }

    public void setGeneId(int geneId) {
        this.geneId = geneId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClinicalTrialGene that = (ClinicalTrialGene) o;

        if (clinicalTrailId != that.clinicalTrailId) return false;
        if (geneId != that.geneId) return false;
        if (clinicalTrialKey != null ? !clinicalTrialKey.equals(that.clinicalTrialKey) : that.clinicalTrialKey != null)
            return false;
        if (geneKey != null ? !geneKey.equals(that.geneKey) : that.geneKey != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = clinicalTrialKey != null ? clinicalTrialKey.hashCode() : 0;
        result = 31 * result + (geneKey != null ? geneKey.hashCode() : 0);
        result = 31 * result + clinicalTrailId;
        result = 31 * result + geneId;
        return result;
    }
}
