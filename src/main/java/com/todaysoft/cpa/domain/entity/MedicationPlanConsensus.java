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
@Table(name = "kt_medication_plan_consensus")
public class MedicationPlanConsensus {
    private String planConsensusKey;
    private String planMethodKey;
    private Integer medicationPlanId;
    private Integer theCategory;
    private String citeMessage;
    private String theUrl;
    private String classification;

    @Id
    @Column(name = "plan_consensus_key")
    public String getPlanConsensusKey() {
        return planConsensusKey;
    }

    public void setPlanConsensusKey(String planConsensusKey) {
        this.planConsensusKey = planConsensusKey;
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
    @Column(name = "the_category")
    public Integer getTheCategory() {
        return theCategory;
    }

    public void setTheCategory(Integer theCategory) {
        this.theCategory = theCategory;
    }

    @Basic
    @Column(name = "cite_message")
    public String getCiteMessage() {
        return citeMessage;
    }

    public void setCiteMessage(String citeMessage) {
        this.citeMessage = citeMessage;
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

    @Basic
    @Column(name = "classification")
    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanConsensus that = (MedicationPlanConsensus) o;
        return Objects.equals(planConsensusKey, that.planConsensusKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicationPlanId, that.medicationPlanId) &&
                Objects.equals(theCategory, that.theCategory) &&
                Objects.equals(citeMessage, that.citeMessage) &&
                Objects.equals(theUrl, that.theUrl) &&
                Objects.equals(classification, that.classification);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planConsensusKey, planMethodKey, medicationPlanId, theCategory, citeMessage, theUrl, classification);
    }
}
