package com.todaysoft.cpa.domain.entity;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/9/12 9:47
 */
@Entity
@Table(name = "kt_plan_study")
public class PlanStudy {
    private String planStudyKey;
    private String medicationPlanKey;
    private Integer medicationPlanId;
    @JSONField(name = "pmid")
    private Integer pmid;
    @JSONField(name = "study")
    private String theStudy;
    private String theEfficacy;
    @JSONField(name = "evidence")
    private String theEvidence;
    private String theComparator;

    @Id
    @Column(name = "plan_study_key", nullable = false, length = 64)
    public String getPlanStudyKey() {
        return planStudyKey;
    }

    public void setPlanStudyKey(String planStudyKey) {
        this.planStudyKey = planStudyKey;
    }

    @Basic
    @Column(name = "medication_plan_key", nullable = true, length = 64)
    public String getMedicationPlanKey() {
        return medicationPlanKey;
    }

    public void setMedicationPlanKey(String medicationPlanKey) {
        this.medicationPlanKey = medicationPlanKey;
    }

    @Basic
    @Column(name = "medication_plan_id", nullable = false)
    public Integer getMedicationPlanId() {
        return medicationPlanId;
    }

    public void setMedicationPlanId(Integer medicationPlanId) {
        this.medicationPlanId = medicationPlanId;
    }

    @Basic
    @Column(name = "pmid", nullable = true)
    public Integer getPmid() {
        return pmid;
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    @Basic
    @Column(name = "the_study", nullable = true, length = 200)
    public String getTheStudy() {
        return theStudy;
    }

    public void setTheStudy(String theStudy) {
        this.theStudy = theStudy;
    }

    @Basic
    @Column(name = "the_efficacy", nullable = true, length = 200)
    public String getTheEfficacy() {
        return theEfficacy;
    }

    public void setTheEfficacy(String theEfficacy) {
        this.theEfficacy = theEfficacy;
    }

    @Basic
    @Column(name = "the_evidence", nullable = true, length = 200)
    public String getTheEvidence() {
        return theEvidence;
    }

    public void setTheEvidence(String theEvidence) {
        this.theEvidence = theEvidence;
    }

    @Basic
    @Column(name = "the_comparator", nullable = true, length = 200)
    public String getTheComparator() {
        return theComparator;
    }

    public void setTheComparator(String theComparator) {
        this.theComparator = theComparator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlanStudy planStudy = (PlanStudy) o;

        if (medicationPlanId != null ? !medicationPlanId.equals(planStudy.medicationPlanId) : planStudy.medicationPlanId != null) return false;
        if (planStudyKey != null ? !planStudyKey.equals(planStudy.planStudyKey) : planStudy.planStudyKey != null)
            return false;
        if (medicationPlanKey != null ? !medicationPlanKey.equals(planStudy.medicationPlanKey) : planStudy.medicationPlanKey != null)
            return false;
        if (pmid != null ? !pmid.equals(planStudy.pmid) : planStudy.pmid != null) return false;
        if (theStudy != null ? !theStudy.equals(planStudy.theStudy) : planStudy.theStudy != null) return false;
        if (theEfficacy != null ? !theEfficacy.equals(planStudy.theEfficacy) : planStudy.theEfficacy != null)
            return false;
        if (theEvidence != null ? !theEvidence.equals(planStudy.theEvidence) : planStudy.theEvidence != null)
            return false;
        if (theComparator != null ? !theComparator.equals(planStudy.theComparator) : planStudy.theComparator != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = planStudyKey != null ? planStudyKey.hashCode() : 0;
        result = 31 * result + (medicationPlanKey != null ? medicationPlanKey.hashCode() : 0);
        result = 31 * result + medicationPlanId;
        result = 31 * result + (pmid != null ? pmid.hashCode() : 0);
        result = 31 * result + (theStudy != null ? theStudy.hashCode() : 0);
        result = 31 * result + (theEfficacy != null ? theEfficacy.hashCode() : 0);
        result = 31 * result + (theEvidence != null ? theEvidence.hashCode() : 0);
        result = 31 * result + (theComparator != null ? theComparator.hashCode() : 0);
        return result;
    }
}
