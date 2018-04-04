package com.todaysoft.cpa.domain.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author lichy
 * @version 2018/4/4
 * @desc
 */
@Entity
@Table(name = "kt_medication_plan_study")
public class MedicationPlanStudy {
    private String planStudyKey;
    private String planMethodKey;
    private Integer medicationPlanId;
    private Integer pmid;
    private String theStudy;
    private String theEfficacy;
    private String theEvidence;
    private String theComparator;
    private String theUrl;

    @Id
    @Column(name = "plan_study_key")
    public String getPlanStudyKey() {
        return planStudyKey;
    }

    public void setPlanStudyKey(String planStudyKey) {
        this.planStudyKey = planStudyKey;
    }

    @Basic
    @Column(name = "plan_method_key")
    public String getPlanMethodKey() {
        return planMethodKey;
    }

    public void setPlanMethodKey(String planMethodKey) {
        this.planMethodKey = planMethodKey;
    }

    @Basic
    @Column(name = "medication_plan_id")
    public Integer getMedicationPlanId() {
        return medicationPlanId;
    }

    public void setMedicationPlanId(Integer medicationPlanId) {
        this.medicationPlanId = medicationPlanId;
    }

    @Basic
    @Column(name = "pmid")
    public Integer getPmid() {
        return pmid;
    }

    public void setPmid(Integer pmid) {
        this.pmid = pmid;
    }

    @Basic
    @Column(name = "the_study")
    public String getTheStudy() {
        return theStudy;
    }

    public void setTheStudy(String theStudy) {
        this.theStudy = theStudy;
    }

    @Basic
    @Column(name = "the_efficacy")
    public String getTheEfficacy() {
        return theEfficacy;
    }

    public void setTheEfficacy(String theEfficacy) {
        this.theEfficacy = theEfficacy;
    }

    @Basic
    @Column(name = "the_evidence")
    public String getTheEvidence() {
        return theEvidence;
    }

    public void setTheEvidence(String theEvidence) {
        this.theEvidence = theEvidence;
    }

    @Basic
    @Column(name = "the_comparator")
    public String getTheComparator() {
        return theComparator;
    }

    public void setTheComparator(String theComparator) {
        this.theComparator = theComparator;
    }

    @Basic
    @Column(name = "the_url")
    @Type(type = "text")
    public String getTheUrl() {
        return theUrl;
    }

    public void setTheUrl(String theUrl) {
        this.theUrl = theUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanStudy that = (MedicationPlanStudy) o;
        return Objects.equals(planStudyKey, that.planStudyKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicationPlanId, that.medicationPlanId) &&
                Objects.equals(pmid, that.pmid) &&
                Objects.equals(theStudy, that.theStudy) &&
                Objects.equals(theEfficacy, that.theEfficacy) &&
                Objects.equals(theEvidence, that.theEvidence) &&
                Objects.equals(theComparator, that.theComparator) &&
                Objects.equals(theUrl, that.theUrl);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planStudyKey, planMethodKey, medicationPlanId, pmid, theStudy, theEfficacy, theEvidence, theComparator, theUrl);
    }
}
