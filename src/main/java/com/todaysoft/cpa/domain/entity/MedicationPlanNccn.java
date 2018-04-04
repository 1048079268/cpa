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
@Table(name = "kt_medication_plan_nccn")
public class MedicationPlanNccn {
    private String planNccnKey;
    private String planMethodKey;
    private Integer medicationPlanId;
    private Integer theCategory;
    private String citeMessage;
    private String theUrl;
    private String theClassification;

    @Id
    @Column(name = "plan_nccn_key")
    public String getPlanNccnKey() {
        return planNccnKey;
    }

    public void setPlanNccnKey(String planNccnKey) {
        this.planNccnKey = planNccnKey;
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
    @Column(name = "the_classification")
    public String getTheClassification() {
        return theClassification;
    }

    public void setTheClassification(String theClassification) {
        this.theClassification = theClassification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MedicationPlanNccn that = (MedicationPlanNccn) o;
        return Objects.equals(planNccnKey, that.planNccnKey) &&
                Objects.equals(planMethodKey, that.planMethodKey) &&
                Objects.equals(medicationPlanId, that.medicationPlanId) &&
                Objects.equals(theCategory, that.theCategory) &&
                Objects.equals(citeMessage, that.citeMessage) &&
                Objects.equals(theUrl, that.theUrl) &&
                Objects.equals(theClassification, that.theClassification);
    }

    @Override
    public int hashCode() {

        return Objects.hash(planNccnKey, planMethodKey, medicationPlanId, theCategory, citeMessage, theUrl, theClassification);
    }
}
